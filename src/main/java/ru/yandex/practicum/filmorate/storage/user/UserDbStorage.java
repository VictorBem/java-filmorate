package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.utility.EntityNoExistException;
import ru.yandex.practicum.filmorate.utility.ValidationException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Set;


@Component
@Primary
@Slf4j
public class UserDbStorage implements UserStorage {

    private static final int INITIAL_STATUS_ID_OF_FRIENDSHIP = 1;
    private static final int CONFIRMED_STATUS_ID_OF_FRIENDSHIP = 1;
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //Метод возвращает всех пользователей из БД
    @Override
    public List<User> getUsers() {
        //Запрос для получения всех пользователей
        String sql = "SELECT * " +
                     "FROM USERS;";

        List<User> users = jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));

        if(users.isEmpty()) {
            return Collections.emptyList();
        }

        //Добавляем каждому пользователю его друзей
        sql = "SELECT * " +
              "FROM FRIEND;";

        jdbcTemplate.query(sql, (rs, rowNum) -> addFriendToUser(users, rs));

        return users;
    }

    //Служебный метод по созданию пользователя из ResultSet
    private User makeUser(ResultSet rs) throws SQLException {
        return new User(rs.getInt("id"),
                rs.getString("email"),
                rs.getString("login"),
                rs.getString("name"),
                rs.getDate("birthday").toLocalDate());
    }

    //Служебный метод для добавления друзей пользователю из ResultSet
    private int addFriendToUser(List<User> users, ResultSet rs) throws SQLException {
        //Получаем ID пользователя и друга
        int currentUserId = rs.getInt("user_id_from");
        int newFriendId = rs.getInt("user_id_to");

        if(newFriendId <= 0) {
            log.error("Id пользователя должно быть больше нуля, передано newFriendId: " + newFriendId);
            throw new ValidationException("Id пользователя должно быть больше нуля, передано newFriendId: " + newFriendId);
        }
        if (currentUserId <= 0) {
            log.error("Id пользователя должно быть больше нуля, передано currentUserId: " + currentUserId);
            throw new ValidationException("Id пользователя должно быть больше нуля, передано currentUserId: " + currentUserId);
        }

        users.stream()
             .filter(u -> u.getId() == currentUserId)
             .findAny()
             .orElseThrow()
             .getFriends()
             .add(newFriendId);

        return newFriendId;
    }

    //Метод для добавления нового пользователя в БД
    @Override
    public User addUser(User user) {

        //Если пользователь существует, то выбрасываем исключение
        if(getUsers().stream().anyMatch(user::equals)) {
            log.error("Переданы некорректные данные о пользователе, проверти данные полей");
            throw new EntityNoExistException("Переданы некорректные данные о пользователи, проверьте данные полей");
        }

        //Если не передано имя пользователя, то в качестве имени используем логин
        if(user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
            log.debug("У пользователя не указано Имя, поэтому используем Логин вместо имени id = {}, login = {}", user.getId(), user.getLogin());
        }

        //Добавляем нового пользователя в таблицу хранящую пользователей (USERS)
        String sql = "INSERT INTO USERS (EMAIL, LOGIN, NAME, BIRTHDAY) " +
                     "VALUES (?, ?, ?, ?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        String finalSql = sql;

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(finalSql, new String[]{"id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, java.sql.Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);

        user.setId(keyHolder.getKey().intValue());

        //Сохраняем в таблице FRIEND информацию о друзьях пользователя
        String sqlInsert = "INSERT INTO FRIEND (USER_ID_FROM, USER_ID_TO, STATUS_ID) " +
                           "VALUES (?, ?, ?);";

        for(int currentFriend : user.getFriends()) {
            jdbcTemplate.update(sqlInsert, user.getId(), currentFriend, INITIAL_STATUS_ID_OF_FRIENDSHIP);
        }

        return user;
    }

    //Метод для изменения пользователя
    @Override
    public User changeUser(User user) {
        //Если пользователь не существует, то выбрасываем исключение
        if(getUsers().stream().noneMatch(user::equals)) {
            log.error("Не найден пользователь с id = {} name = {} для обновления", user.getId(), user.getName());
            throw new EntityNoExistException("Переданы некорректные данные о пользователе, проверьте данные полей");
        }
        //Формируем и выполняем запрос на обновление записи пользователя
        String sql = "UPDATE USERS " +
                     "SET EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY =? " +
                     "WHERE ID = ?;";

        jdbcTemplate.update(sql,
                            user.getEmail(),
                            user.getLogin(),
                            user.getName(),
                            user.getBirthday(),
                            user.getId());

        //удаляем существующие записи по друзьям из таблицы и вносим новые
        sql = "DELETE FROM FRIEND " +
              "WHERE USER_ID_FROM = ?;";

        jdbcTemplate.update(sql, user.getId());

        //добавляем обновленных друзей
        sql = "INSERT INTO FRIEND (USER_ID_FROM, USER_ID_TO, STATUS_ID) " +
              "VALUES (?, ?, ?);";

        Set<Integer> newFriends = user.getFriends();//.stream().map(Friend::getId).collect(Collectors.toSet());
        user.getFriends().removeAll(user.getFriends());

        for(int currentId : newFriends) {
            jdbcTemplate.update(sql,
                                user.getId(),
                                currentId,
                                INITIAL_STATUS_ID_OF_FRIENDSHIP);
            user.getFriends().add(currentId);
        }

        return user;
    }

    //Метод добавляет пользователя в друзья
    public void addToFriend(int userId, int friendId) {
        String sql = "INSERT INTO FRIEND (USER_ID_FROM, USER_ID_TO, STATUS_ID) " +
                     "VALUES (?, ?, ?);";

        jdbcTemplate.update(sql, userId, friendId, INITIAL_STATUS_ID_OF_FRIENDSHIP);
    }

    //Метод удаляет пользователя из друзей
    public void removeFromFriend(int userId, int friendId) {
        String sql = "DELETE FROM FRIEND " +
                     "WHERE USER_ID_FROM = ? " +
                     "AND USER_ID_TO = ?;";

        jdbcTemplate.update(sql, userId, friendId);
    }

    //Метод подтверждает дружбу
    public void confirmFriend(int userId, int friendId) {
        String sql = "UPDATE FRIEND " +
                     "SET STATUS_ID = ? " +
                     "WHERE USER_ID_FROM = ? " +
                     "  AND USER_ID_TO = ?;";

        jdbcTemplate.update(sql, CONFIRMED_STATUS_ID_OF_FRIENDSHIP, userId, friendId);
    }

}

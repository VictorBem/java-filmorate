package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserDbStorageTest {
    private final UserDbStorage userDbStorage;

    //Тестируем создание пользователя
    @Test
    public void createUserTest() {

        //Создаем пользователя для тестирования
        User userForTest = new User(1,
                "mail@yandex.ru",
                "test_user",
                "TestUser",
                LocalDate.of(1980, 10, 21));

        User createdUser = userDbStorage.addUser(userForTest);
        List<User> createdUsersFromDb = userDbStorage.getUsers();

        //Проверяем корректность данных пользователя в БД
        assertEquals(1, createdUsersFromDb.size(), "Создано некорректное количество пользователей");
        assertEquals(createdUsersFromDb.get(0).getId(), userForTest.getId(), "Пользователь создан с некорректным Id");
        assertEquals(createdUsersFromDb.get(0).getName(), userForTest.getName(), "Пользователь создан с некорректным именем");
        assertEquals(createdUsersFromDb.get(0).getLogin(), userForTest.getLogin(), "Пользователь создан с некорректным логином");
        assertEquals(createdUsersFromDb.get(0).getEmail(), userForTest.getEmail(), "Пользователь создан с некорректным адресом электронной почты");
        assertEquals(createdUsersFromDb.get(0).getBirthday(), userForTest.getBirthday(), "Пользователь создан с некорректной датой рождения");

        //Проверяем корректность данных пользователя возвращенного методом записи пользователя в БД
        assertEquals(createdUser.getId(), userForTest.getId(), "Пользователь создан с некорректным Id");
        assertEquals(createdUser.getName(), userForTest.getName(), "Пользователь создан с некорректным именем");
        assertEquals(createdUser.getLogin(), userForTest.getLogin(), "Пользователь создан с некорректным логином");
        assertEquals(createdUser.getEmail(), userForTest.getEmail(), "Пользователь создан с некорректным адресом электронной почты");
        assertEquals(createdUser.getBirthday(), userForTest.getBirthday(), "Пользователь создан с некорректной датой рождения");
    }

    //Тестируем создание пользователя
    @Test
    public void changeUserTest() {

        //Создаем пользователя для тестирования
        User userForTest = new User(1,
                "mail@yandex.ru",
                "test_user",
                "TestUser",
                LocalDate.of(1980, 10, 21));

        User createdUser = userDbStorage.addUser(userForTest);

        //Вносим изменения в пользователя
        User changedUserForTest = new User(1,
                "changed_mail@yandex.ru",
                "changed_test_user",
                "changed_TestUser",
                LocalDate.of(1985, 5, 25));

        User changedUser = userDbStorage.changeUser(changedUserForTest);
        List<User> changedUserFromDb = userDbStorage.getUsers();

        //Проверяем корректность данных пользователя в БД
        assertEquals(1, changedUserFromDb.size(), "Изменилось количество пользователей в БД");
        assertEquals(changedUserFromDb.get(0).getId(), changedUserForTest.getId(), "Пользователь создан с некорректным Id");
        assertEquals(changedUserFromDb.get(0).getName(), changedUserForTest.getName(), "Пользователь создан с некорректным именем");
        assertEquals(changedUserFromDb.get(0).getLogin(), changedUserForTest.getLogin(), "Пользователь создан с некорректным логином");
        assertEquals(changedUserFromDb.get(0).getEmail(), changedUserForTest.getEmail(), "Пользователь создан с некорректным адресом электронной почты");
        assertEquals(changedUserFromDb.get(0).getBirthday(), changedUserForTest.getBirthday(), "Пользователь создан с некорректной датой рождения");

        //Проверяем корректность данных пользователя возвращенного методом изменения пользователя в БД
        assertEquals(changedUserForTest.getId(), changedUser.getId(), "Пользователь создан с некорректным Id");
        assertEquals(changedUserForTest.getName(), changedUser.getName(), "Пользователь создан с некорректным именем");
        assertEquals(changedUserForTest.getLogin(), changedUser.getLogin(), "Пользователь создан с некорректным логином");
        assertEquals(changedUserForTest.getEmail(), changedUser.getEmail(), "Пользователь создан с некорректным адресом электронной почты");
        assertEquals(changedUserForTest.getBirthday(), changedUser.getBirthday(), "Пользователь создан с некорректной датой рождения");
    }

    //Тестируем добавление пользователя в друзья
    @Test
    public void addFriendTest() {
        //Создаем пользователя для тестирования
        User userForTest = new User(1,
                "mail@yandex.ru",
                "test_user",
                "TestUser",
                LocalDate.of(1980, 10, 21));

       userDbStorage.addUser(userForTest);

        //Вносим еще одного пользователя для его добавления в друзья
        User friendUserForTest = new User(2,
                "changedmail@yandex.ru",
                "changedtest_user",
                "changedTestUser",
                LocalDate.of(1985, 5, 25));

       userDbStorage.addUser(friendUserForTest);

        //Добавляем пользователя в друзья
        userDbStorage.addToFriend(userForTest.getId(), friendUserForTest.getId());

        //Считываем пользователя и проверяем, что у него появился друг
        List<User> users = userDbStorage.getUsers();

        assertEquals(users.get(0).getFriends().size(), 1, "Должен быть один друг");
        assertEquals(users.get(0).getFriends().iterator().next().intValue(), friendUserForTest.getId(), "Неверный ИД друга");
        //Проверяем, что у друга пользователь не является другом
        assertEquals(users.get(1).getFriends().size(), 0, "Не должно быть один друзей");
    }

    //Удаляем пользователя из друзей
    @Test
    public void removeFriendTest() {
        //Создаем пользователя для тестирования
        User userForTest = new User(1,
                "mail@yandex.ru",
                "test_user",
                "TestUser",
                LocalDate.of(1980, 10, 21));

        userDbStorage.addUser(userForTest);

        //Вносим еще одного пользователя для его добавления в друзья
        User friendUserForTest = new User(2,
                "changedmail@yandex.ru",
                "changedtest_user",
                "changedTestUser",
                LocalDate.of(1985, 5, 25));

        userDbStorage.addUser(friendUserForTest);

        //Добавляем пользователя в друзья
        userDbStorage.addToFriend(userForTest.getId(), friendUserForTest.getId());

        //Считываем пользователя и проверяем, что у него появился друг
        List<User> users = userDbStorage.getUsers();

        assertEquals(users.get(0).getFriends().size(), 1, "Должен быть один друг");
        assertEquals(users.get(0).getFriends().iterator().next().intValue(), friendUserForTest.getId(), "Неверный ИД друга");
        //Проверяем, что у друга пользователь не является другом
        assertEquals(users.get(1).getFriends().size(), 0, "Не должно быть друзей");

        //удаляем пользователя из друзей и проверяем отсутствие друзей
        userDbStorage.removeFromFriend(userForTest.getId(), friendUserForTest.getId());
        users = userDbStorage.getUsers();
        assertEquals(users.get(0).getFriends().size(), 0, "У пользователя больше нет друзей");
        //Проверяем, что у друга пользователь не является другом
        assertEquals(users.get(1).getFriends().size(), 0, "Не должно быть друзей");
    }


}

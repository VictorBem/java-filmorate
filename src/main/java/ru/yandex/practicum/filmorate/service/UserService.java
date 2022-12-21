package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.utility.SameUserAndFriendException;
import ru.yandex.practicum.filmorate.utility.UserNoExistException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    //Добавление пользователя в друзья
    public void addToFriend(int userId, int friendId) {
        //Проверяем, что пользователь и друг существует
        checkUserAndFriend(userId, friendId);
        //Если пользователь решил стать другом самуму себе - не допускаем этого
        if(userId == friendId) {
            throw new SameUserAndFriendException("Для пользователя и друга указан один и тот же id: " + userId);
        }

        //Добавляем пользователю нового друга - не забыть обработать NoSuchElementException
        userStorage.getUsers()
                   .stream()
                   .filter(u -> u.getId() == userId)
                   .findFirst()
                   .orElseThrow()
                   .getFriends()
                   .add(friendId);

        //Делаем обратную операцию добавляем другу пользователя в друзья
        userStorage.getUsers()
                   .stream()
                   .filter(u -> u.getId() == friendId)
                   .findFirst()
                   .orElseThrow()
                   .getFriends()
                   .add(userId);
    }

    //Удаление пользователя из друзей
    public void removeFromFriend(int userId, int friendId) {
        //Проверяем, что пользователь и друг существует
        checkUserAndFriend(userId, friendId);

        //Удаляем у пользователя друга
        userStorage.getUsers()
                .stream()
                .filter(u -> u.getId() == userId)
                .findFirst()
                .orElseThrow()
                .getFriends()
                .remove((Integer) friendId);

        //Делаем обратную операцию удаляем у друга пользователя
        userStorage.getUsers()
                .stream()
                .filter(u -> u.getId() == friendId)
                .findFirst()
                .orElseThrow()
                .getFriends()
                .remove((Integer) userId);
    }

    //Метод возвращает список друзей пользователя
    public List<User> getFriends(int userId) {
        //Проверяем существование пользователя
        if (!checkUser(userId)) {
            throw new UserNoExistException("Пользователя с id:" + userId + " не существует.");
        }

        //Поскольку у друзей пользователя сам пользователь включен в друзья - то выберем только тех пользователей для которых пользователь друг
        return userStorage.getUsers()
                          .stream()
                          .filter(u -> u.getFriends().contains((Integer) userId))
                          .collect(Collectors.toList());
    }

    //Метод возвращает список друзей общих с другим пользователем
    public List<User> getCommonFriends(int userId, int friendId) {
        //Проверяем существование пользователя
        if (!checkUser(userId)) {
            throw new UserNoExistException("Пользователя с id:" + userId + " не существует.");
        }
        if (!checkUser(friendId)) {
            throw new UserNoExistException("Друга с id:" + friendId + " не существует.");
        }

        if(userStorage.getUsers()
                      .stream()
                      .filter(u -> u.getId() == userId || u.getId() == friendId)
                      .anyMatch(u -> u.getFriends() == null || u.getFriends().isEmpty())) {
            return new ArrayList<>();
        }
        //Фильтруем только пользователей которые одновременно в друзьях у пользователя и его друга
        return  userStorage.getUsers()
                           .stream()
                           .filter(u -> u.getFriends().contains((Integer) userId) && u.getFriends().contains((Integer) friendId))
                           .collect(Collectors.toList());
    }

    //Получаем пользователя по id
    public User getUser(int id) {
        return userStorage.getUsers()
                          .stream()
                          .filter(u -> u.getId() == id)
                          .findFirst()
                          .orElseThrow();
    }

    //Проверяем, что пользователь существует
    public boolean checkUser(int id) {
        return userStorage.getUsers().stream().anyMatch(u -> u.getId() == id);
    }
    //Проверяем существование пользователя и его друга
    public void checkUserAndFriend(int userId, int friendId) {
        //Проверяем, что пользователь существует
        if(!checkUser(userId)) {
            throw new UserNoExistException("Пользователя с id:" + userId + " не существует.");
        }
        //Проверяем что id пользователя-друга существует
        if(!checkUser(friendId)) {
            throw new UserNoExistException("Друга пользователя с id:" + friendId + " не существует.");
        }
    }

    //Получение списка всех пользователей
    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    //Добавление пользователя
    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    //Изменение пользователя
    public User changeUser(User user) {
        return  userStorage.changeUser(user);
    }

}

package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.utility.EntityNoExistException;
import ru.yandex.practicum.filmorate.utility.SameUserAndFriendException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
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
        if (userId == friendId) {
            log.error("Пользователь не может дружить сам с собой id пользователя: " + userId);
            throw new SameUserAndFriendException("Для пользователя и друга указан один и тот же id: " + userId);
        }

        ((UserDbStorage) userStorage).addToFriend(userId, friendId);

    }

    //Метод подтверждения дружбы
    public void confirmFriend(int userId, int friendId) {
        //Проверяем, что пользователь и друг существует
        checkUserAndFriend(userId, friendId);
        //Если пользователь решил стать другом самуму себе - не допускаем этого
        if (userId == friendId) {
            log.error("Пользователь не может дружить сам с собой id пользователя: " + userId);
            throw new SameUserAndFriendException("Для пользователя и друга указан один и тот же id: " + userId);
        }

        ((UserDbStorage) userStorage).confirmFriend(userId, friendId);

    }

    //Удаление пользователя из друзей
    public void removeFromFriend(int userId, int friendId) {
        //Проверяем, что пользователь и друг существует
        checkUserAndFriend(userId, friendId);

        ((UserDbStorage) userStorage).removeFromFriend(userId, friendId);
    }

    //Метод возвращает список друзей пользователя
    public List<User> getFriends(int userId) {
        //Проверяем существование пользователя
        if (!checkUser(userId)) {
            log.error("Пользователя с id:" + userId + " не существует.");
            throw new EntityNoExistException("Пользователя с id:" + userId + " не существует.");
        }

        List<User> currentUsers = userStorage.getUsers();

        User currentUser = currentUsers.stream()
                .filter(u -> u.getId() == userId)
                .findAny()
                .orElseThrow();

        return currentUsers.stream()
                .filter(u -> currentUser.getFriends().contains(u.getId()))
                .collect(Collectors.toList());

    }

    //Метод возвращает список друзей общих с другим пользователем
    public List<User> getCommonFriends(int userId, int friendId) {
        //Проверяем существование пользователя
        if (!checkUser(userId)) {
            log.error("Пользователя с id:" + userId + " не существует.");
            throw new EntityNoExistException("Пользователя с id:" + userId + " не существует.");
        }
        if (!checkUser(friendId)) {
            log.error("Друга с id:" + friendId + " не существует.");
            throw new EntityNoExistException("Друга с id:" + friendId + " не существует.");
        }

        List<User> currentUsers = userStorage.getUsers();

        if (currentUsers.stream()
                .filter(u -> u.getId() == userId || u.getId() == friendId)
                .anyMatch(u -> u.getFriends() == null || u.getFriends().isEmpty())) {
            return new ArrayList<>();
        }

        //Фильтруем множества друзей первого и второго пользователя, далее оставляем только пересечение множеств
        Set<Integer> firstUserFriends = currentUsers.stream()
                .filter(u -> u.getId() == userId)
                .findFirst()
                .orElseThrow()
                .getFriends();

        Set<Integer> secondUserFriends = currentUsers.stream()
                .filter(u -> u.getId() == friendId)
                .findFirst()
                .orElseThrow()
                .getFriends();

        firstUserFriends.retainAll(secondUserFriends);


        return currentUsers.stream()
                .filter(u -> firstUserFriends.contains(u.getId()))
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
        if (!checkUser(userId)) {
            log.error("Пользователя с id:" + userId + " не существует.");
            throw new EntityNoExistException("Пользователя с id:" + userId + " не существует.");
        }
        //Проверяем что id пользователя-друга существует
        if (!checkUser(friendId)) {
            log.error("Друга пользователя с id:" + friendId + " не существует.");
            throw new EntityNoExistException("Друга пользователя с id:" + friendId + " не существует.");
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
        return userStorage.changeUser(user);
    }

}

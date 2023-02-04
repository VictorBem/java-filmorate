package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.utility.EntityAlreadyExistException;
import ru.yandex.practicum.filmorate.utility.EntityNoExistException;
import ru.yandex.practicum.filmorate.utility.ValidationException;

import java.util.*;

@Slf4j
@RestController()
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    UserController(UserService userService) {
        this.userService = userService;
    }

    //Получение списка пользователей
    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

    //Создание пользователя
    @PostMapping
    public User addUser(@Valid @RequestBody User user) throws ValidationException, EntityAlreadyExistException {
        return userService.addUser(user);
    }

    //Изменение пользователя
    @PutMapping
    public User changeUser(@Valid @RequestBody User user) throws ValidationException, EntityNoExistException {
        return userService.changeUser(user);
    }

    //Добавляем друга
    @PutMapping("/{id}/friends/{friendId}")
    public void addToFriend(@PathVariable("id") int userId, @PathVariable int friendId) {
        userService.addToFriend(userId, friendId);
    }

    //Удаление друга
    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFromFriend(@PathVariable("id") int userId, @PathVariable int friendId) {
        userService.removeFromFriend(userId, friendId);
    }

    //Получаем пользователя по его id
    @GetMapping("/{id}")
    public User getUser(@PathVariable("id") int userId) {
        return userService.getUser(userId);
    }

    //Получаем список друзей пользователя
    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable("id") int userId) {
        return userService.getFriends(userId);
    }

    //Получаем список общих с другим пользователем
    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable("id") int userId, @PathVariable("otherId") int friendId) {
        return userService.getCommonFriends(userId, friendId);
    }

    //Подтверждаем дружбу
    @PutMapping("/{id}/confirm/{friendId}")
    public void confirmFriend(@PathVariable("id") int userId, @PathVariable int friendId) {
        userService.confirmFriend(userId, friendId);
    }

}

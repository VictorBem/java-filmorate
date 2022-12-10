package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.utility.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.utility.UserNoExistException;
import ru.yandex.practicum.filmorate.utility.ValidationException;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController()
@RequestMapping("/users")
public class UserController {
    //Map для хранения пользователей
    private Map<Integer, User> users = new HashMap<>();
    private int currentUserId = 0;

    //метод возвращающий доступный идентификатор пользователя
    private int getUserId() {
        return ++currentUserId;
    }

    //Метод для обработки get запросов
    @GetMapping
    public List<User> getFilms() {
        return new ArrayList<>((Collection<User>) users.values());
    }

    //Метод для обработки post запросов
    @PostMapping
    public User addUser(@Valid @RequestBody User user) throws ValidationException, UserAlreadyExistException {
        //Проверяем корректность данных пользователя
        if (!isUserCorrect(user)) {
            log.error("Переданы некорректные данные о пользователе, проверти данные полей");
            throw new ValidationException("Переданы некорректные данные о пользователи, проверьте данные полей");
        }

        //Если пользователь уже существует, то выбрасываем исключение
        for (User currentUser : users.values()) {
            if (currentUser.equals(user)) {
                log.error("Переданы некорректные данные о пользователе, проверти данные полей");
                throw new UserAlreadyExistException("Переданы некорректные данные о пользователи, проверьте данные полей");
            }
        }

        //Если не передано имя пользователя, то в качестве имени используем логин
        if(user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
            log.debug("У пользователя не указано Имя, поэтому используем Логин вместо имени id = {}, login = {}", user.getId(), user.getLogin());
        }

        user.setId(getUserId());
        users.put(user.getId(), user);
        log.debug("Создан новый пользователь id = {}, name = {}", user.getId(), user.getName());
        return users.get(user.getId());
    }

    //Метод для обработки put запросов
    @PutMapping
    public User changeUser(@Valid @RequestBody User user) throws ValidationException, UserNoExistException {
        //Проверяем корректность данных пользователя
        if (!isUserCorrect(user)) {
            log.error("Переданы некорректные данные о пользователе, проверти данные полей");
            throw new ValidationException("Переданы некорректные данные о пользователи, проверьте данные полей");
        }
        //Если пользователь уже существует, то обновляем данные пользователя
        if (users.containsKey(user.getId())) {
            users.replace(user.getId(), user);
            log.debug("Обновлен пользователь id = {}, name = {}", user.getId(), user.getName());
        } else {
            log.debug("Не найден пользователь с id = {} name = {} для обновления", user.getId(), user.getName());
            throw new UserNoExistException("Переданы некорректные данные о пользователе, проверьте данные полей");
        }
        return users.get(user.getId());
    }

    /*Метод проверяющий корректность пользователя в запросе - создан по ТЗ, но использоваться фактически не будет, так
      как вся проверка проводится через аннотации указанные для соответсвующих полей класса User и все ошибочные данные
      будут выявлены до данной проверки.
     */
    private boolean isUserCorrect(User user) {
        return !user.getEmail().isEmpty()
                && user.getEmail().contains("@")
                && !user.getLogin().isEmpty()
                && !user.getLogin().contains(" ")
                && user.getBirthday().isBefore(LocalDate.now());
    }
}

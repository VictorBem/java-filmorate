package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.bind.annotation.PatchMapping;
import ru.yandex.practicum.filmorate.validator.Birthday;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class User {
    //идентификатор пользователя
    @EqualsAndHashCode.Exclude
    private int id;

    //адрес электронной почты пользователя
    @NotBlank(message = "Адрес электронной почты не должен быть пустым")
    @Email(message = "Указан некорректный адрес электронной почты.")
    private String email;

    @NotBlank(message = "Логин не должен быть пустым")
    @Pattern(regexp = "^\\S*$", message = "Пробел в логине не допустим") //"/^\\S*$/"
    //логин пользователя
    private String login;

    //имя пользователя
    private String name;

    //дата рождения пользователя
    @Birthday
    @Past(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;
}

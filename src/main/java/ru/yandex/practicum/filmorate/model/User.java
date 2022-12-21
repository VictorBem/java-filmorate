package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.validator.Birthday;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
public class User {
    //идентификатор пользователя
    @EqualsAndHashCode.Exclude
    private int id;

    //id друзей пользователя
    private final Set<Integer> friends = new HashSet<>();

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

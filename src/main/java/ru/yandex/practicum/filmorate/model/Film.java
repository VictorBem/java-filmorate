package ru.yandex.practicum.filmorate.model;


import javax.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.validator.ReleaseDate;

import java.time.LocalDate;
import java.util.*;

@Data
@AllArgsConstructor
public class Film{
    /*идентификатор фильма, который с учетом структуры БД является уникальным ключом,
    все прочие поля в прямой зависимости от ключа*/
    private int id;

    //id пользователей поставивших лайк фильму
    @EqualsAndHashCode.Exclude
    private final Set<Integer> likes = new HashSet<>();

    //название фильма
    @NotBlank(message = "Наименование фильма должно быть заполнено.")
    @EqualsAndHashCode.Exclude
    private String name;

    //описание фильма

    @NotBlank(message = "Описание фильма должно быть заполнено.")
    @Size(max = 200, message = "Описание фильма не должно превышать 200 символов.")
    @EqualsAndHashCode.Exclude
    private String description;


    //дата выхода фильма в прокат
    @ReleaseDate
    @EqualsAndHashCode.Exclude
    private LocalDate releaseDate;

    //длительность фильма
    @Positive(message = "Длительность фильма должна быть положительной")
    @EqualsAndHashCode.Exclude
    private int duration;

    //рейтинг фильма
    @EqualsAndHashCode.Exclude
    private final Rating mpa;

    //Жанры фильма
    @EqualsAndHashCode.Exclude
    //private final Set<Genre> genres = new HashSet<>();
    //Будем использовать TreeSet что бы пройти тесты в postman - для тестов важен строгий порядок жанров
    private final TreeSet<Genre> genres = new TreeSet<>();

}

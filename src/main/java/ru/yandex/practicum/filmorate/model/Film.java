package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import ru.yandex.practicum.filmorate.validator.ReleaseDate;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Film {
    //идентификатор фильма

    @EqualsAndHashCode.Exclude
    private int id;

    //название фильма
    @NotBlank(message = "Наименование фильма должно быть заполнено.")
    private String name;

    //описание фильма

    @NotBlank(message = "Описание фильма должно быть заполнено.")
    @Size(max = 200, message = "Описание фильма не должно превышать 200 символов.")
    private String description;


    //дата выхода фильма в прокат
    @ReleaseDate
    private LocalDate releaseDate;

    //длительность фильма
    @Positive(message = "Длительность фильма должна быть положительной")
    private int duration;
}

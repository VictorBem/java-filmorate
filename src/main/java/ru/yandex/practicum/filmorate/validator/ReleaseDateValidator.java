package ru.yandex.practicum.filmorate.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

//Валидатор для проверки того, что релиз фильма состоялся после 1895.12.28
public class ReleaseDateValidator implements ConstraintValidator<ReleaseDate, LocalDate> {
    final private static LocalDate DATE_OF_FIRST_FILM = LocalDate.of(1895,12,28);
    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        try {
            return !localDate.isBefore(DATE_OF_FIRST_FILM);
        } catch (Exception e) {
            return false;
        }
    }
}

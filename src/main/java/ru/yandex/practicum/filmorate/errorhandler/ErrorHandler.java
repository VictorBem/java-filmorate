package ru.yandex.practicum.filmorate.errorhandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.utility.*;

import java.util.NoSuchElementException;

@RestControllerAdvice("ru.yandex.practicum")
@Slf4j
public class ErrorHandler {

    @ExceptionHandler({EntityNoExistException.class,
                       NoSuchElementException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse ErrorResponseNotFound(RuntimeException e) {
        log.error("Ошибка: " + e.getMessage());
        return new ErrorResponse("Ошибка.", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse ErrorResponseValidation(ValidationException e) {
        log.error("Ошибка: " + e.getMessage());
        return new ErrorResponse("Ошибка.", e.getMessage());
    }

    @ExceptionHandler({EntityAlreadyExistException.class,
                       IncorrectCountException.class,
                       SameUserAndFriendException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse ErrorResponseOtherException(RuntimeException e) {
        log.error("Ошибка: " + e.getMessage());
        return new ErrorResponse("Ошибка.", e.getMessage());
    }
}

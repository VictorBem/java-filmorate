package ru.yandex.practicum.filmorate.errorhandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.utility.*;

import java.util.NoSuchElementException;

@RestControllerAdvice("ru.yandex.practicum")
public class ErrorHandler {

    @ExceptionHandler({FilmNoExistException.class,
                       LikeNoExistException.class,
                       UserNoExistException.class,
                       NoSuchElementException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse ErrorResponseNotFound(RuntimeException e) {
        return new ErrorResponse("Ошибка.", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse ErrorResponseValidation(ValidationException e) {
        return new ErrorResponse("Ошибка.", e.getMessage());
    }

    @ExceptionHandler({FilmAlreadyExistException.class,
                       IncorrectCountException.class,
                       LikeAlreadyExistException.class,
                       SameUserAndFriendException.class,
                       UserAlreadyExistException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse ErrorResponseOtherException(RuntimeException e) {
        return new ErrorResponse("Ошибка.", e.getMessage());
    }
}

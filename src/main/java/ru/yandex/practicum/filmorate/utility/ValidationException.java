package ru.yandex.practicum.filmorate.utility;

public class ValidationException extends RuntimeException{
    public ValidationException() {
    }

    public ValidationException(final String message){
        super(message);
    }
}

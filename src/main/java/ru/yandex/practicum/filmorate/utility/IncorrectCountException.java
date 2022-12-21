package ru.yandex.practicum.filmorate.utility;

public class IncorrectCountException extends RuntimeException{
    public IncorrectCountException() {

    }

    public IncorrectCountException(String message) {
        super(message);
    }
}

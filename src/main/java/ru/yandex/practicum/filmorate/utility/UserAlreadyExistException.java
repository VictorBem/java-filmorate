package ru.yandex.practicum.filmorate.utility;

public class UserAlreadyExistException extends RuntimeException{
    public UserAlreadyExistException() {
    }

    public UserAlreadyExistException(final String message){
        super(message);
    }
}


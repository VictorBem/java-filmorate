package ru.yandex.practicum.filmorate.utility;

public class UserAlreadyExistException extends Exception{
    public UserAlreadyExistException(){
    }

    public UserAlreadyExistException(final String message){
        super(message);
    }
}


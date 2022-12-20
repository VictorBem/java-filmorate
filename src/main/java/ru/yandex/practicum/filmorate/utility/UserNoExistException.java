package ru.yandex.practicum.filmorate.utility;

public class UserNoExistException extends RuntimeException{
    public UserNoExistException(){
    }

    public UserNoExistException(final String message){
        super(message);
    }
}

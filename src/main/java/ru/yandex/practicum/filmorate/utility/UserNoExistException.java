package ru.yandex.practicum.filmorate.utility;

public class UserNoExistException extends Exception{
    public UserNoExistException(){
    }

    public UserNoExistException(final String message){
        super(message);
    }
}

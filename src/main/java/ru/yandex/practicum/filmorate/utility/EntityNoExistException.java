package ru.yandex.practicum.filmorate.utility;

public class EntityNoExistException extends RuntimeException{
    public EntityNoExistException() {
    }

    public EntityNoExistException(final String message){
        super(message);
    }
}

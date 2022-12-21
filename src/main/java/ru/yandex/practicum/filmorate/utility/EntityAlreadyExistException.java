package ru.yandex.practicum.filmorate.utility;

public class EntityAlreadyExistException extends RuntimeException{
    public EntityAlreadyExistException() {
    }

    public EntityAlreadyExistException(final String message){
        super(message);
    }
}

package ru.yandex.practicum.filmorate.utility;

public class LikeAlreadyExistException extends RuntimeException{
    public LikeAlreadyExistException() {
    }
    public LikeAlreadyExistException(String message) {
        super(message);
    }
}

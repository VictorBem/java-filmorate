package ru.yandex.practicum.filmorate.utility;

public class LikeNoExistException extends RuntimeException{
    public LikeNoExistException() {
    }

    public LikeNoExistException(String message) {
        super(message);
    }
}

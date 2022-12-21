package ru.yandex.practicum.filmorate.utility;

public class SameUserAndFriendException extends RuntimeException{
    public SameUserAndFriendException() {
    }

    public SameUserAndFriendException(String message) {
        super(message);
    }
 }

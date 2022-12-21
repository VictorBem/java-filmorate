package ru.yandex.practicum.filmorate.utility;

public class FilmAlreadyExistException extends RuntimeException{
    public FilmAlreadyExistException() {
    }

    public FilmAlreadyExistException(final String message){
        super(message);
    }
}

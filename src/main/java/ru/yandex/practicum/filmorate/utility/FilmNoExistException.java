package ru.yandex.practicum.filmorate.utility;

public class FilmNoExistException extends RuntimeException{
    public FilmNoExistException() {
    }

    public FilmNoExistException(final String message){
        super(message);
    }
}

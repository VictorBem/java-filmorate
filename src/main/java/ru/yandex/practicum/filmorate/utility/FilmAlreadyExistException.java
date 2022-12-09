package ru.yandex.practicum.filmorate.utility;

public class FilmAlreadyExistException extends Exception{
    public FilmAlreadyExistException(){
    }

    public FilmAlreadyExistException(final String message){
        super(message);
    }
}

package ru.yandex.practicum.filmorate.utility;

public class FilmNoExistException extends Exception{
    public FilmNoExistException(){
    }

    public FilmNoExistException(final String message){
        super(message);
    }
}

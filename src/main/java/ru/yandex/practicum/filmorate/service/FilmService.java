package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.utility.*;

import java.util.List;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    //метод позволяющий пользователю поставить лайк фильму
    public void addLike(int filmId, int userId) {
        //проверяем корректность id указанных фильма и пользователя
        verifyFilmAndUser(filmId, userId);

        //Если пользователь уже поставил лайк фильму выбрасываем исключение
        if (filmStorage.getFilmById(filmId).getLikes().contains(userId)) {
            log.error("Пользователь с id: " + userId + " уже поставил лайк фильму с id: " + filmId);
            throw new EntityAlreadyExistException("Пользователь с id: " + userId + " уже поставил лайк фильму с id: " + filmId);
        }
        //Ставим лайк фильму
        ((FilmDbStorage) filmStorage).addLike(filmId, userId);
    }

    //метод позволяющий пользователю поставить удалить лайк
    public void removeLike(int filmId, int userId) {
        //проверяем корректность id указанных фильма и пользователя
        verifyFilmAndUser(filmId, userId);

        //Если пользователь не ставил лайк фильму выбрасываем исключение
        if(!filmStorage.getFilmById(filmId).getLikes().contains(userId)) {
            log.error("Пользователь с id: " + userId + "не ставил лайк фильму с id: " + filmId);
            throw new EntityNoExistException("Пользователь с id: " + userId + "не ставил лайк фильму с id: " + filmId);
        }
        //удаляем лайк
        ((FilmDbStorage) filmStorage).removeLike(filmId, userId);
    }

    //получение наиболее популярных фильмов
    public List<Film> getMostPopularFilms(int count) {
        //Запрос не может требовать вывести отрицательное количество фильмов
        if (count <= 0 ) {
            log.error("Указано некорректное количество фильмов для выбора, count <= 0");
            throw new IncorrectCountException("Указано некорректное количество фильмов для выбора, count <= 0");
        }

        //Невозможно вывести больше фильмов чем есть в базе
        int currentQuantityOfFilms = ((FilmDbStorage) filmStorage).getQuantityOfFilms();
        if(count >= currentQuantityOfFilms) {
            count = currentQuantityOfFilms;
        }

        return ((FilmDbStorage) filmStorage).getMostPopularFilms(count);
    }

    //Метод возвращает фильм по его id
    public Film getFilm(int id) {
        return filmStorage.getFilmById(id);
    }

    //получение всех фильмов
    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    //Добавление фильма в базу данных
    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    //Изменение фильма в базе даных
    public Film changeFilm(Film film) {
        return filmStorage.changeFilm(film);
    }

    //Служебный метод для верификации существования фильма и пользователя
    private void verifyFilmAndUser(int filmId, int userId) {
        //Если фильма нет в базе выбрасываем исключение
        try {
            filmStorage.getFilmById(filmId);
        } catch (EntityNoExistException | IncorrectCountException exp) {
            log.error("Невозможно поставить лайк так как указан некорректный id фильма: " + filmId);
            throw new EntityNoExistException("Невозможно поставить лайк так как указан некорректный id фильма: " + filmId);
        }
        //Если пользователя нет в базе выбрасываем исключение
        try {
            userStorage.getUserById(userId);
        } catch (EntityNoExistException | IncorrectCountException exp) {
            log.error("Невозможно поставить лайк так как указан некорректный id фильма: " + filmId);
            throw new EntityNoExistException("Невозможно поставить лайк так как указан некорректный id фильма: " + filmId);
        }
    }
}

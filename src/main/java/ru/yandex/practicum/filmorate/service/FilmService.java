package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.utility.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
        if (filmStorage.getFilms().stream().filter(f -> f.getId() == filmId).findFirst().orElseThrow().getLikes().contains(userId)) {
            log.error("Пользователь с id: " + userId + " уже поставил лайк фильму с id: " + filmId);
            throw new EntityAlreadyExistException("Пользователь с id: " + userId + " уже поставил лайк фильму с id: " + filmId);
        }
        //добавляем лайк
        filmStorage.getFilms()
                   .stream()
                   .filter(f -> f.getId() == filmId)
                   .findFirst()
                   .orElseThrow()
                   .getLikes()
                   .add(userId);
    }

    //метод позволяющий пользователю поставить удалить лайк
    public void removeLike(int filmId, int userId) {
        //проверяем корректность id указанных фильма и пользователя
        verifyFilmAndUser(filmId, userId);

        //Если пользователь не ставил лайк фильму выбрасываем исключение
        if(!filmStorage.getFilms().stream().filter(f -> f.getId() == filmId).findFirst().orElseThrow().getLikes().contains(userId)) {
            log.error("Пользователь с id: " + userId + "не ставил лайк фильму с id: " + filmId);
            throw new EntityNoExistException("Пользователь с id: " + userId + "не ставил лайк фильму с id: " + filmId);
        }
        //удаляем лайк
        filmStorage.getFilms()
                .stream()
                .filter(f -> f.getId() == filmId)
                .findFirst()
                .orElseThrow()
                .getLikes()
                .remove(userId);
    }

    //получение наиболее популярных фильмов
    public List<Film> getMostPopularFilms(int count) {
        //Запрос не может требовать вывести отрицательное количество фильмов
        if (count <= 0 ) {
            log.error("Указано некорректное количество фильмов для выбора, count <= 0");
            throw new IncorrectCountException("Указано некорректное количество фильмов для выбора, count <= 0");
        }
        //Невозможно вывести больше фильмов чем есть в базе
        if(count >= filmStorage.getFilms().size()) {
            count = filmStorage.getFilms().size();
        }

        return filmStorage.getFilms()
                          .stream()
                          .sorted(Comparator.comparingInt(o -> o.getLikes().size() * -1))
                          .limit(count)
                          .collect(Collectors.toList());
    }

    public Film getFilm(int id) {
        return filmStorage.getFilms().stream().filter(f -> f.getId() == id).findFirst().orElseThrow();
    }

    //получение всех фильмов
    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film changeFilm(Film film) {
        return filmStorage.changeFilm(film);
    }

    private void verifyFilmAndUser(int filmId, int userId) {
        //Если фильма нет в базе выбрасываем исключение
        if(filmStorage.getFilms().stream().noneMatch(f -> f.getId() == filmId)) {
            log.error("Невозможно поставить лайк так как указан некорректный id фильма: " + filmId);
            throw new EntityNoExistException("Невозможно поставить лайк так как указан некорректный id фильма: " + filmId);
        }

        //Если пользователя нет в базе выбрасываем исключение
        if(userStorage.getUsers().stream().noneMatch(u -> u.getId() == userId)) {
            log.error("Невозможно поставить лайк так как указан некорректный id пользователя: " + userId);
            throw new EntityNoExistException("Невозможно поставить лайк так как указан некорректный id пользователя: " + userId);
        }
    }


}

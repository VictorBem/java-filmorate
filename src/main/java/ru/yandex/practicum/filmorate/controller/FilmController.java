package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.utility.*;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    //private final InMemoryFilmStorage inMemoryFilmStorage;
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    //Возвращает список фильмов
    @GetMapping
    public List<Film> getFilms() {
        return filmService.getFilms();
    }

    //Возвращает фильм по его номеру
    @GetMapping("/{id}")
    public Film getFilm(@PathVariable("id") int id) {
        return filmService.getFilm(id);
    }

    //Создаем новый фильм
    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) throws ValidationException, FilmAlreadyExistException {
        return filmService.addFilm(film);
    }

    //Изменяем фильм
    @PutMapping
    public Film changeFilm(@Valid  @RequestBody Film film) throws ValidationException, FilmNoExistException {
        return filmService.changeFilm(film);
    }

    //Добавляем лайк фильму
    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable("id") int filmId, @PathVariable int userId) {
        filmService.addLike(filmId, userId);
    }

    //Удаляем лайк поставленный фильму
    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable("id") int filmId, @PathVariable int userId) {
        filmService.removeLike(filmId, userId);
    }

    //Удаляем лайк поставленный фильму
    @GetMapping("/popular")
    public List<Film> getMostPopularFilms(@RequestParam(name = "count", defaultValue = "10") int count) {
        return filmService.getMostPopularFilms(count);
    }

}

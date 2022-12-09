package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.utility.*;

import java.time.LocalDate;
import java.util.*;

@Data
@Slf4j
@RestController
@RequestMapping(value = "/films")
public class FilmController {
    //Map для хранения фильмов
    private Map<Integer, Film> films = new HashMap<>();

    private int currentFilmId = 0;

    //метод возвращающий доступный идентификатор фильма
    private int getFilmId() {
        return ++currentFilmId;
    }

    //Метод для обработки get запросов - возвращает список фильмов
    @GetMapping
    public List<Film> getFilms() {
        return new ArrayList<>((Collection<Film>) films.values());
    }

    //Метод для обработки post запросов
    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) throws ValidationException, FilmAlreadyExistException {
        //Проверяем получены ли корректные данные о фильме и если нет выбрасываем исключение и записываем ошибку в лог
        if (!isFilmCorrect(film)) {
            log.error("Переданы некорректные данные о фильме, проверьте данные полей");
            throw new ValidationException("Переданы некорректные данные о фильме, проверьте значения полей");
        }
        //Если фильм уже существует, то выбрасываем исключение
        for (Film currentFilm : films.values()) {
            if (currentFilm.equals(film)) {
                log.error("Переданы некорректные данные о фильме, проверьте данные полей");
                throw new FilmAlreadyExistException("Переданы некорректные данные о фильме, проверьте данные полей");
            }
        }
        //Добавляем фильм в базу фильмов
        film.setId(getFilmId());
        films.put(film.getId(), film);
        log.debug("Создан новый фильм id = {}, name = {}", film.getId(), film.getName());
        return films.get(film.getId());
    }

    //Метод для обработки put запросов
    @PutMapping
    public Film changeFilm(@Valid  @RequestBody Film film) throws ValidationException, FilmNoExistException {
        //Проверяем корректные ли данные о фильме были получены
        if (!isFilmCorrect(film)) {
            log.error("Переданы некорректные данные о фильме, проверьте данные полей");
            throw new ValidationException("Переданы некорректные данные о фильме, проверьте значения полей");
        }
        //Если фильм уже есть в базе фильмов, то обновляем данные по фильму, в противном случае создаем новую запись
        if (films.containsKey(film.getId())) {
            films.replace(film.getId(), film);
            log.debug("Обновлен фильм id = {}, name = {}", film.getId(), film.getName());
        } else {
            log.debug("Не найден фильм с id = {} name = {} для обновления", film.getId(), film.getName());
            throw new FilmNoExistException("Переданы некорректные данные о фильме, проверьте данные полей");
        }
        return films.get(film.getId());
    }

    /*Метод проверяет корректность информации о фильме - есть название, описание не больше 200 символов,
      дата выхода фильма не раньше чем 28.12.1895, длительность положительная

      Метод создан по ТЗ, но использоваться фактически не будет, так как вся проверка проводится через аннотации
      указанные для соответсвующих полей класса Film и все ошибочные данные будут выявлены до данной проверки.
     */
    private boolean isFilmCorrect(Film film) {
        return !film.getName().isEmpty()
                && film.getDescription().length() <= 200
                && film.getReleaseDate().isAfter(LocalDate.of(1895, 12, 28))
                && film.getDuration() > 0;
    }
}

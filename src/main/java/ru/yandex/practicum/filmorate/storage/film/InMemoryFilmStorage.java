package ru.yandex.practicum.filmorate.storage.film;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.utility.EntityAlreadyExistException;
import ru.yandex.practicum.filmorate.utility.EntityNoExistException;
import ru.yandex.practicum.filmorate.utility.ValidationException;

import java.time.LocalDate;
import java.util.*;

@Component
@Getter
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private int currentFilmId = 0;

    //метод возвращающий доступный идентификатор фильма
    private int getFilmId() {
        return ++currentFilmId;
    }

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>((Collection<Film>) films.values());
    }

    @Override
    public Film addFilm(Film film) throws ValidationException, EntityAlreadyExistException {
        //Проверяем получены ли корректные данные о фильме и если нет выбрасываем исключение и записываем ошибку в лог
        if (!isFilmCorrect(film)) {
            log.error("Переданы некорректные данные о фильме, проверьте данные полей");
            throw new ValidationException("Переданы некорректные данные о фильме, проверьте значения полей");
        }
        //Если фильм уже существует, то выбрасываем исключение
        if (films.values().stream().anyMatch(film::equals)) {
            log.error("Переданы некорректные данные о фильме, проверьте данные полей");
            throw new EntityAlreadyExistException("Переданы некорректные данные о фильме, проверьте данные полей");
        }
        //Добавляем фильм в базу фильмов
        film.setId(getFilmId());
        films.put(film.getId(), film);
        log.debug("Создан новый фильм id = {}, name = {}", film.getId(), film.getName());
        return films.get(film.getId());
    }

    @Override
    public Film changeFilm(Film film) throws ValidationException, EntityNoExistException {
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
            throw new EntityNoExistException("Переданы некорректные данные о фильме, проверьте данные полей");
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

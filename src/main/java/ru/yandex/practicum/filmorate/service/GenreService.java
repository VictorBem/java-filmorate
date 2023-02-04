package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;

import java.util.List;

@Service
@Slf4j
public class GenreService {
    private final GenreDbStorage genreDbStorage;

    @Autowired
    public GenreService(GenreDbStorage genreDbStorage) {
        this.genreDbStorage = genreDbStorage;
    }

    //Метод возвращает жанр по его Id
    public Genre getGenre(int id) {
        log.info("Получение жанра с id: " + id);
        return  genreDbStorage.getGenresById(id);
    }

    //Метод возвращает список всех жанров
    public List<Genre> getGenres() {
        log.info("Получение всех жанров из БД.");
        return genreDbStorage.getGenres();
    }

}

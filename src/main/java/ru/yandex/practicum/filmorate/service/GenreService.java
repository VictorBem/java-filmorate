package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;

import java.util.List;

@Service
public class GenreService {
    private final GenreDbStorage genreDbStorage;

    @Autowired
    public GenreService(GenreDbStorage genreDbStorage) {
        this.genreDbStorage = genreDbStorage;
    }

    //Метод возвращает жанр по его Id
    public Genre getGenre(int id) {
        return genreDbStorage.getGenres().stream().filter(r -> r.getId() == id).findFirst().orElseThrow();
    }

    //Метод возвращает список всех жанров
    public List<Genre> getGenres() {
        return genreDbStorage.getGenres();
    }

}

package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class GenreDbStorageTest {
    private final GenreDbStorage genreDbStorage;
    @Test
    public void testGetAllGenres() {

        List<Genre> genreList = genreDbStorage.getGenres();
        assertEquals(6, genreList.size(), "В базе всего 6 жанров фильмов");
        assertEquals(new Genre(1, "Комедия"), genreList.get(0), "Некорректный жанр 1 - Комедия");
        assertEquals(new Genre(2, "Драма"), genreList.get(1), "Некорректный жанр 2 - Драма");
        assertEquals(new Genre(3, "Мультфильм"), genreList.get(2), "Некорректный жанр 3 - Мультфильм");
        assertEquals(new Genre(4, "Триллер"), genreList.get(3), "Некорректный жанр 4 - Триллер");
        assertEquals(new Genre(5, "Документальный"), genreList.get(4), "Некорректный жанр  5 - Документальный");
        assertEquals(new Genre(6, "Боевик"), genreList.get(5), "Некорректный рейтинг 6 - Боевик");
    }

}

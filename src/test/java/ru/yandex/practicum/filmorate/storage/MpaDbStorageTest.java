package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class MpaDbStorageTest {

    private final MpaDbStorage mpaDbStorage;
    @Test
    public void testGetAllRatings() {

        List<Rating> ratingList = mpaDbStorage.getRatings();
        assertEquals(5, ratingList.size(), "В базе всего 5 возрастных рейтингов фильмов");
        assertEquals(new Rating(1, "G"), ratingList.get(0), "Некорректный рейтинг фильма 1 - G");
        assertEquals(new Rating(2, "PG"), ratingList.get(1), "Некорректный рейтинг фильма 2 - PG");
        assertEquals(new Rating(3, "PG-13"), ratingList.get(2), "Некорректный рейтинг фильма 3 - PG13");
        assertEquals(new Rating(4, "R"), ratingList.get(3), "Некорректный рейтинг фильма 4 - R");
        assertEquals(new Rating(5, "NC-17"), ratingList.get(4), "Некорректный рейтинг фильма 5 - NC-17");

    }

}

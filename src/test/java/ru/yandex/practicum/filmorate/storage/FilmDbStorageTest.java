package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import ru.yandex.practicum.filmorate.utility.EntityNoExistException;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FilmDbStorageTest {
    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;

    //Тестируем создание фильма
    @Test
    public void createFilmTest() {
        //Создаем фильм в базе данных
        Film filmForTest = new Film(1,
                                 "Тестовый фильм",
                              "Описание тестового фильма",
                                        LocalDate.now(),
                                120,
                                         new Rating(1, "G"));

        Film createdFilm = filmDbStorage.addFilm(filmForTest);
        List<Film> createdFilmsFromDb = filmDbStorage.getFilms();

        assertEquals(createdFilmsFromDb.size(), 1, "Создано некорректное количество фильмов");
        assertEquals(createdFilmsFromDb.get(0).getId(), filmForTest.getId(), "Фильм создан с некорректным Id");
        assertEquals(createdFilmsFromDb.get(0).getName(), filmForTest.getName(), "Фильм создан с некорректным наименованием");
        assertEquals(createdFilmsFromDb.get(0).getDescription(), filmForTest.getDescription(), "Фильм создан с некорректным описанием");
        assertEquals(createdFilmsFromDb.get(0).getDuration(), filmForTest.getDuration(), "Фильм создан с некорректной длительностью");
        assertEquals(createdFilmsFromDb.get(0).getReleaseDate(), filmForTest.getReleaseDate(), "Фильм создан с некорректной датой выхода в прокат");
        assertEquals(createdFilmsFromDb.get(0).getMpa(), filmForTest.getMpa(), "Фильм создан с некорректным рейтингом");

        assertEquals(createdFilm.getId(), filmForTest.getId(), "Метод создания вернул фильм с некорректным Id");
        assertEquals(createdFilm.getName(), filmForTest.getName(), "Метод создания вернул фильм с некорректным наименованием");
        assertEquals(createdFilm.getDescription(), filmForTest.getDescription(), "Метод создания вернул фильм с некорректным описанием");
        assertEquals(createdFilm.getDuration(), filmForTest.getDuration(), "Метод создания вернул фильм с некорректной длительностью");
        assertEquals(createdFilm.getReleaseDate(), filmForTest.getReleaseDate(), "Метод создания вернул фильм с некорректной датой выхода в прокат");
        assertEquals(createdFilm.getMpa(), filmForTest.getMpa(), "Метод создания вернул фильм с некорректным рейтингом");


    }

    //Тестируем изменение фильма
    @Test
    public void changeFilmTestExistedFilmId() {
        //Создаем фильм в базе данных
        Film filmForTest = new Film(1,
                "Тестовый фильм",
                "Описание тестового фильма",
                LocalDate.now(),
                120,
                new Rating(1, "G"));

        Film createdFilm = filmDbStorage.addFilm(filmForTest);

        //Изменяем созданный фильм
        Film filmForTestChanges = new Film(1,
                "Тестовый фильм обновленный",
                "Описание тестового фильма обновленное",
                LocalDate.now(),
                150,
                new Rating(2, "PG"));

        Film changedFilm = filmDbStorage.changeFilm(filmForTestChanges);
        List<Film> changedFilmsFromDb = filmDbStorage.getFilms();

        assertEquals(changedFilmsFromDb.size(), 1, "Изменилось количество фильмов в базе данных");
        assertEquals(changedFilmsFromDb.get(0).getId(), filmForTestChanges.getId(), "Некорректный Id");
        assertEquals(changedFilmsFromDb.get(0).getName(), filmForTestChanges.getName(), "Некорректное изменение наименования");
        assertEquals(changedFilmsFromDb.get(0).getDescription(), filmForTestChanges.getDescription(), "Некорректное изменение описания");
        assertEquals(changedFilmsFromDb.get(0).getDuration(), filmForTestChanges.getDuration(), "Некорректное изменение длительности");
        assertEquals(changedFilmsFromDb.get(0).getReleaseDate(), filmForTestChanges.getReleaseDate(), "Некорректное изменение даты выхода в прокат");
        assertEquals(changedFilmsFromDb.get(0).getMpa(), filmForTestChanges.getMpa(), "Некорректное изменение рейтинга");

        assertEquals(changedFilm.getId(), filmForTestChanges.getId(), "Метод изменения вернул фильм с некорректным Id");
        assertEquals(changedFilm.getName(), filmForTestChanges.getName(), "Метод изменения вернул фильм с некорректным наименованием");
        assertEquals(changedFilm.getDescription(), filmForTestChanges.getDescription(), "Метод изменения вернул фильм с некорректным описанием");
        assertEquals(changedFilm.getDuration(), filmForTestChanges.getDuration(), "Метод изменения вернул фильм с некорректной длительностью");
        assertEquals(changedFilm.getReleaseDate(), filmForTestChanges.getReleaseDate(), "Метод изменения вернул фильм с некорректной датой выхода в прокат");
        assertEquals(changedFilm.getMpa(), filmForTestChanges.getMpa(), "Метод изменения вернул фильм с некорректным рейтингом");
    }

    //Тестируем изменение несуществующего фильма
    @Test
    public void changeFilmTestWithNotExistedFilmId() {
        Film filmForTest = new Film(1,
                "Тестовый фильм",
                "Описание тестового фильма",
                LocalDate.now(),
                120,
                new Rating(1, "G"));

        filmDbStorage.addFilm(filmForTest);

        Film filmForTestChanges = new Film(9999,
                "Тестовый фильм обновленный",
                "Описание тестового фильма обновленное",
                LocalDate.now(),
                150,
                new Rating(2, "PG"));
        try {
            Film changedFilm = filmDbStorage.changeFilm(filmForTestChanges);
        } catch (EntityNoExistException exp) {
            assertEquals(exp.getMessage(), "Не найден фильм с id = 9999 для обновления", "Изменить несуществующий фильм нельзя");
        }

        List<Film> filmsFromDb = filmDbStorage.getFilms();

        assertEquals(1, filmsFromDb.size(), "Изменилось количество фильмов в базе данных");
        assertEquals(filmsFromDb.get(0).getId(), filmForTest.getId(), "Произошло изменение фильма в БД");
        assertEquals(filmsFromDb.get(0).getName(), filmForTest.getName(), "Произошло изменение фильма в БД");
        assertEquals(filmsFromDb.get(0).getDescription(), filmForTest.getDescription(), "Произошло изменение фильма в БД");
        assertEquals(filmsFromDb.get(0).getDuration(), filmForTest.getDuration(), "Произошло изменение фильма в БД");
        assertEquals(filmsFromDb.get(0).getReleaseDate(), filmForTest.getReleaseDate(), "Произошло изменение фильма в БД");
        assertEquals(filmsFromDb.get(0).getMpa(), filmForTest.getMpa(), "Произошло изменение фильма в БД");
    }

    //Тестируем лайк фильму
    @Test
    public void likeTest(){
        //Создаем фильм для тестирования
        Film filmForTest = new Film(1,
                "Тестовый фильм",
                "Описание тестового фильма",
                LocalDate.now(),
                120,
                new Rating(1, "G"));

        filmDbStorage.addFilm(filmForTest);

        //Создаем пользователя для тестирования

        User userForTest = new User(1,
                "mail@yandex.ru",
                "test_user",
                "TestUser",
                LocalDate.of(1980, 10,21));

        userDbStorage.addUser(userForTest);
        //Ставим фильму лайк и проверяем результат
        filmDbStorage.addLike(filmForTest.getId(), userForTest.getId());
        Film filmWithLike = filmDbStorage.getFilms().get(0);

        assertEquals(1, filmWithLike.getLikes().size(), "Количество лайков фильма должно равняться 1");
    }

    //Тестируем удаление лайка
    @Test
    public void removeLikeTest(){
        //Создаем фильм для тестирования
        Film filmForTest = new Film(1,
                "Тестовый фильм",
                "Описание тестового фильма",
                LocalDate.now(),
                120,
                new Rating(1, "G"));

        filmDbStorage.addFilm(filmForTest);

        //Создаем пользователя для тестирования
        User userForTest = new User(1,
                "mail@yandex.ru",
                "test_user",
                "TestUser",
                LocalDate.of(1980, 10,21));

        userDbStorage.addUser(userForTest);
        //Ставим фильму лайк и проверяем результат
        filmDbStorage.addLike(filmForTest.getId(), userForTest.getId());
        Film filmWithLike = filmDbStorage.getFilms().get(0);

        assertEquals(1, filmWithLike.getLikes().size(), "Количество лайков фильма должно равняться 1");

        filmDbStorage.removeLike(filmForTest.getId(), userForTest.getId());
        filmWithLike = filmDbStorage.getFilms().get(0);
        assertEquals(0, filmWithLike.getLikes().size(), "Количество лайков фильма должно равняться 0");
    }

}

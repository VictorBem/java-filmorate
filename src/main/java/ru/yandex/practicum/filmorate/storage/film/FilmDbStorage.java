package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.utility.EntityNoExistException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Primary
@Slf4j
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    private final GenreDbStorage genreDbStorage;

    //public FilmDbStorage(JdbcTemplate jdbcTemplate, FilmStorage filmStorage) {
    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, GenreDbStorage genreDbStorage) { //FilmStorage filmStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreDbStorage = genreDbStorage;
    }

    @Override
    public List<Film> getFilms() {

        String sql = "SELECT * " +
                     "FROM FILM " +
                     "LEFT OUTER JOIN RATING ON FILM.RATING_ID = RATING.ID;";

        List<Film> films = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));

        if (films.isEmpty()) {
            return Collections.emptyList();
        }

        sql = "SELECT * " +
              "FROM LIKES;";

        jdbcTemplate.query(sql, (rs, rowNum) -> makeLike(rs, films));

        sql = "SELECT * " +
              "FROM FILM_GENRE " +
              "JOIN GENRE ON FILM_GENRE.GENRE_ID = GENRE.ID;";

        jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs, films));

        return films;
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        return new Film(rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("releasedate").toLocalDate(),
                rs.getInt("duration"),
                new Rating(rs.getInt("rating.id"),
                        rs.getString("rating.name")));
    }

    private Film makeLike(ResultSet rs, List<Film> films) throws SQLException {

        Film currentFilm = films.stream()
                .filter(f -> {
                    try {
                        return f.getId() == rs.getInt("film_id");
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                })
                .findAny()
                .orElseThrow();

        currentFilm.getLikes().add(rs.getInt("user_id"));

        return currentFilm;
    }

    private Film makeGenre(ResultSet rs, List<Film> films) throws SQLException {

        Film currentFilm = films.stream()
                .filter(f -> {
                    try {
                        return f.getId() == rs.getInt("film_id");
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                })
                .findAny()
                .orElseThrow();

        currentFilm.getGenres().add(new Genre(rs.getInt("genre_id"),
                rs.getString("genre.name")));

        return currentFilm;
    }

    //Метод для добавления фильма в базу данных
    @Override
    public Film addFilm(Film film) {
        //Формируем строку с запросом для добавления фильма в БД
        String sql = "INSERT INTO FILM (NAME, DESCRIPTION, RELEASEDATE, DURATION, RATING_ID) " +
                     "VALUES (?, ?, ?, ?, ?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        String finalSql = sql;
        //Выполняем запрос - добавляем фильм в БД
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(finalSql, new String[]{"id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, java.sql.Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);

        film.setId(keyHolder.getKey().intValue());

        //Добавляем жанры фильма в БД
        sql = "INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) " +
              "VALUES (?, ?);";

        for (Genre currentGenre : film.getGenres()) {
            jdbcTemplate.update(sql,
                    film.getId(),
                    currentGenre.getId());
        }

        return film;
    }

    //Метод для изменения фильма
    @Override
    public Film changeFilm(Film film) {

        if (getFilms().stream().noneMatch(f -> f.getId() == film.getId())) {
            log.error("Не найден фильм с id = {} для обновления", film.getId());
            throw new EntityNoExistException("Не найден фильм с id = " + film.getId() + " для обновления");
        }
        //Строка запроса ддля изменения фильма
        String sql = "UPDATE FILM " +
                     "SET NAME = ?, DESCRIPTION = ?, RELEASEDATE = ?, DURATION =?, RATING_ID =? " +
                     "WHERE ID = ?;";


        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());

        //Удаляем существующие записи по жанрам фильма из таблицы и вносим новые
        sql = "DELETE FROM FILM_GENRE " +
              "WHERE FILM_ID = ?;";

        jdbcTemplate.update(sql, film.getId());

        //добавляем измененные жанры
        sql = "INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) " +
              "VALUES (?, ?);";

        //Получаем множество уникальных жанров фильма
        Set<Integer> newGenres = film.getGenres().stream().map(Genre::getId).collect(Collectors.toSet());
        //Удаляем все жанры из фильма
        film.getGenres().removeAll(film.getGenres());

        List<Genre> genres = genreDbStorage.getGenres();

        //Вставляем жанры в фильм и в базу данных
        for (int currentId : newGenres) {
            jdbcTemplate.update(sql,
                    film.getId(),
                    currentId);

            film.getGenres().add(new Genre(currentId, genres.stream()
                    .filter(g -> g.getId() == currentId)
                    .findFirst()
                    .orElseThrow()
                    .getName()));
        }

        return film;
    }

    //Метод для добавления лайка фильму
    public void addLike(int filmId, int userId) {
        String sql = "INSERT INTO LIKES (FILM_ID, USER_ID) " +
                     "VALUES (?, ?);";

        jdbcTemplate.update(sql, filmId, userId);
    }

    //Метод для удаления существующего лайка
    public void removeLike(int filmId, int userId) {
        String sql = "DELETE FROM LIKES " +
                     "WHERE FILM_ID = ? " +
                     "AND USER_ID = ?;";

        jdbcTemplate.update(sql, filmId, userId);
    }

}

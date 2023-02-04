package ru.yandex.practicum.filmorate.storage.genre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.utility.EntityNoExistException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class GenreDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //Метод для получения жанров из БД
    public List<Genre> getGenres() {

        String sql = "SELECT * " +
                "FROM GENRE " +
                "ORDER BY ID;";

        List<Genre> genres = jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs));

        if(genres.isEmpty()) {
            return Collections.emptyList();
        }

        return genres;
    }

    public Genre getGenresById(int genreId) {

        String sql = "SELECT * " +
                "FROM GENRE " +
                "WHERE ID = ?;";

        List<Genre> genres = jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs), genreId);

        if(genres.isEmpty()) {
            log.error("Рейтинг с id: " + genreId + " не найден");
            throw new EntityNoExistException("Рейтинг с id: " + genreId + " не найден");
        }

        return genres.get(0);
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        return new Genre(rs.getInt("id"), rs.getString("name"));
    }
}

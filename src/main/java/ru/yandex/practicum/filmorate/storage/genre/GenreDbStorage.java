package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@Component
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

    private Genre makeGenre(ResultSet rs) throws SQLException {
        return new Genre(rs.getInt("id"), rs.getString("name"));
    }
}

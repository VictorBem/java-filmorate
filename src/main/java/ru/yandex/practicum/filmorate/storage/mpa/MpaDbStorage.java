package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@Component
public class MpaDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //Метод для получения рейтингов из БД
    public List<Rating> getRatings() {

        String sql = "SELECT * " +
                     "FROM RATING " +
                     "ORDER BY ID;";

        List<Rating> ratings = jdbcTemplate.query(sql, (rs, rowNum) -> makeRating(rs));

        if(ratings.isEmpty()) {
            return Collections.emptyList();
        }

        return ratings;
    }

    private Rating makeRating(ResultSet rs) throws SQLException {
        return new Rating(rs.getInt("id"), rs.getString("name"));
    }
}


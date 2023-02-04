package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.utility.EntityNoExistException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@Component
@Slf4j
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

    //Метод для получения рейтинга из БД по его id
    public Rating getRatingsById(int ratingId) {

        String sql = "SELECT * " +
                "FROM RATING " +
                "WHERE ID = ?;";

        List<Rating> ratings = jdbcTemplate.query(sql, (rs, rowNum) -> makeRating(rs), ratingId);

        if(ratings.isEmpty()) {
            log.error("Рейтинг с id: " + ratingId + " не найден");
            throw new EntityNoExistException("Рейтинг с id: " + ratingId + " не найден");
        }

        return ratings.get(0);
    }

    private Rating makeRating(ResultSet rs) throws SQLException {
        return new Rating(rs.getInt("id"), rs.getString("name"));
    }
}


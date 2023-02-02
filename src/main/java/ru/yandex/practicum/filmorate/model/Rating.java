package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Objects;


@Getter
public class Rating {
    private int id;

    private String name;
    @JsonCreator
    public Rating(@JsonProperty(value = "id") int id, @JsonProperty(value = "name") String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rating rating = (Rating) o;
        return id == rating.id && Objects.equals(name, rating.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}


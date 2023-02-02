package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;


import java.util.List;

@Service
public class MpaService {
    private final MpaDbStorage mpaStorage;

    @Autowired
    public MpaService(MpaDbStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    //Метод возвращает рейтинг по его Id
    public Rating getRating(int id) {
        return mpaStorage.getRatings().stream().filter(r -> r.getId() == id).findFirst().orElseThrow();
    }

    //Метод возвращает список всех рейтингов
    public List<Rating> getRatings() {
        return mpaStorage.getRatings();
    }

}

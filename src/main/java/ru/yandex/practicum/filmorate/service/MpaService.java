package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;


import java.util.List;

@Service
@Slf4j
public class MpaService {
    private final MpaDbStorage mpaStorage;

    @Autowired
    public MpaService(MpaDbStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    //Метод возвращает рейтинг по его Id
    public Rating getRating(int id) {
        log.info("Получение рейтинга с id: " + id);
        return mpaStorage.getRatingsById(id);
    }

    //Метод возвращает список всех рейтингов
    public List<Rating> getRatings() {
        log.info("Получение всех жанров из БД.");
        return mpaStorage.getRatings();
    }

}

package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;


@RestController
@RequestMapping("/mpa")
public class MpaController {
    private final MpaService mpaService;

    @Autowired
    public MpaController(MpaService mpaService) {
        this.mpaService = mpaService;
    }

    //Метод возвращает список рейтингов
    @GetMapping
    public List<Rating> getRatings() {
        return mpaService.getRatings();
    }

    //Метод возвращает рейтинг по Id
    @GetMapping("/{id}")
    public Rating getRating(@PathVariable("id") int id) {
        return mpaService.getRating(id);
    }

}

package ru.yandex.practicum.filmorate.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.model.Film;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    private HttpResponse<String> response;
    private static Gson gson;
    private static HttpClient client;
    private ConfigurableApplicationContext context;

    @BeforeAll
    static void setUpBeforeAll() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new DateAdapter());
        gson = gsonBuilder.create();

        client = HttpClient.newHttpClient();
    }


    @BeforeEach
    void setUp() {
        context = SpringApplication.run(FilmorateApplication.class);
    }

    //Тест запроса с пустым наименованием фильма
    @Test
    void checkFilmValidationIfFilmNameIsEmpty() {
        response = null;
        URI url = URI.create("http://localhost:8080/films");
        //Отправляем запрос с пустым именем фильма
        Film filmForTest = new Film(1, "","Звездные врата: Команда SG-1", LocalDate.of(2007,06,12), 43);

        String task = gson.toJson(filmForTest);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(task);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(body)
                .uri(url)
                .header("Content-Type","application/json")
                .header("Accept-Charset","utf-8")
                .build();


        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        //Проверяем, что сервер ответил 400 - Bad Request
        assertEquals(400, response.statusCode(), "Сервер вернул некорректный код состояния");
    }

    //Тест с заполненным полем наименование фильма
    @Test
    void checkFilmValidationIfFilmNameNoEmpty() {
        response = null;
        URI url = URI.create("http://localhost:8080/films");
        //Отправляем запрос с пустым именем фильма
        Film filmForTest = new Film(1, "SG-1","Звездные врата: Команда SG-1", LocalDate.of(2007,06,12), 43);

        String task = gson.toJson(filmForTest);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(task);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(body)
                .uri(url)
                .header("Content-Type","application/json")
                .header("Accept-Charset","utf-8")
                .build();


        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }

        JsonElement jsonElement = JsonParser.parseString(response.body());
        Film receivedFilm = gson.fromJson(jsonElement, Film.class);

        assertEquals(200, response.statusCode(), "Сервер вернул некорректный код состояния.");
        assertEquals(filmForTest, receivedFilm, "Создан некорректный фильм.");
    }

    //Тест запроса с описанием более 200 символов
    @Test
    void checkFilmValidationIfFilmDescriptionMore200Symbols() {
        response = null;
        URI url = URI.create("http://localhost:8080/films");
        //Отправляем запрос с пустым именем фильма
        Film filmForTest = new Film(1,
                     "Команда SG-1",
                 "Действие сериала начинается спустя год после событий художественного фильма." +
                           "Сеть устройств, созданных инопланетной расой древних и называемых «звёздными вратами»" +
                           ",позволяет мгновенно перемещаться между различными мирами. Сериал Звёздные врата SG-1" +
                           "— это хроники приключений отряда ЗВ-1 (англ. SG-1), который вместе с другими 24 отрядами" +
                           "(члены которых играют второстепенные и третьестепенные роли в некоторых сериях) исследуют" +
                           "нашу галактику и защищают Землю от различных инопланетных угроз.",
                           LocalDate.of(2007,06,12),
                   43);

        String task = gson.toJson(filmForTest);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(task);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(body)
                .uri(url)
                .header("Content-Type","application/json")
                .header("Accept-Charset","utf-8")
                .build();


        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        //Проверяем, что сервер ответил 400 - Bad Request
        assertEquals(400, response.statusCode(), "Сервер вернул некорректный код состояния");
    }

    //Тест запроса с описанием менее 200 символов
    @Test
    void checkFilmValidationIfFilmDescriptionLess200Symbols() {
        response = null;
        URI url = URI.create("http://localhost:8080/films");
        //Отправляем запрос с пустым именем фильма
        Film filmForTest = new Film(1,
                    "Команда SG-1",
                "Действие сериала начинается спустя год после событий художественного фильма." +
                          "Сеть устройств, созданных инопланетной расой древних и называемых «звёздными вратами»",
                          LocalDate.of(2007,06,12),
                  43);

        String task = gson.toJson(filmForTest);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(task);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(body)
                .uri(url)
                .header("Content-Type","application/json")
                .header("Accept-Charset","utf-8")
                .build();


        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }

        JsonElement jsonElement = JsonParser.parseString(response.body());
        Film receivedFilm = gson.fromJson(jsonElement, Film.class);

        assertEquals(200, response.statusCode(), "Сервер вернул некорректный код состояния.");
        assertEquals(filmForTest, receivedFilm, "Создан некорректный фильм.");
    }

    //Тест с датой выхода фильма ранее минимально возможной
    @Test
    void checkFilmValidationIfReleaseDate16000101() {
        response = null;
        URI url = URI.create("http://localhost:8080/films");
        //Отправляем запрос с пустым именем фильма
        Film filmForTest = new Film(1,
                     "Команда SG-1",
                 "Действие сериала начинается спустя год после событий художественного фильма.",
                            LocalDate.of(1600,01,01),
                    43);

        String task = gson.toJson(filmForTest);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(task);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(body)
                .uri(url)
                .header("Content-Type","application/json")
                .header("Accept-Charset","utf-8")
                .build();


        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        //Проверяем, что сервер ответил 400 - Bad Request
        assertEquals(400, response.statusCode(), "Сервер вернул некорректный код состояния");
    }

    //Тест с датой выхода фильма 2001.08.12
    @Test
    void checkFilmValidationIfReleaseDate20010812() {
        response = null;
        URI url = URI.create("http://localhost:8080/films");
        //Отправляем запрос с пустым именем фильма
        Film filmForTest = new Film(1,
                    "Команда SG-1",
                "Действие сериала начинается спустя год после событий художественного фильма.",
                           LocalDate.of(2001,8,12),
                           43);

        String task = gson.toJson(filmForTest);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(task);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(body)
                .uri(url)
                .header("Content-Type","application/json")
                .header("Accept-Charset","utf-8")
                .build();


        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        JsonElement jsonElement = JsonParser.parseString(response.body());
        Film receivedFilm = gson.fromJson(jsonElement, Film.class);

        assertEquals(200, response.statusCode(), "Сервер вернул некорректный код состояния.");
        assertEquals(filmForTest, receivedFilm, "Создан некорректный фильм.");
    }

    //Тест для фильма с отрицательной длительностью
    @Test
    void checkFilmValidationIfNegativeDuration() {
        response = null;
        URI url = URI.create("http://localhost:8080/films");
        //Отправляем запрос с пустым именем фильма
        Film filmForTest = new Film(1,
                     "Команда SG-1",
                 "Действие сериала начинается спустя год после событий художественного фильма.",
                            LocalDate.of(1991,7,12),
                    -43);

        String task = gson.toJson(filmForTest);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(task);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(body)
                .uri(url)
                .header("Content-Type","application/json")
                .header("Accept-Charset","utf-8")
                .build();


        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        //Проверяем, что сервер ответил 400 - Bad Request
        assertEquals(400, response.statusCode(), "Сервер вернул некорректный код состояния");
    }

    //Тест для фильма с положительной длительностью
    @Test
    void checkFilmValidationIfPositiveDuration() {
        response = null;
        URI url = URI.create("http://localhost:8080/films");
        //Отправляем запрос с пустым именем фильма
        Film filmForTest = new Film(1,
                     "Команда SG-1",
                 "Действие сериала начинается спустя год после событий художественного фильма.",
                            LocalDate.of(1990,01,01),
                    43);

        String task = gson.toJson(filmForTest);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(task);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(body)
                .uri(url)
                .header("Content-Type","application/json")
                .header("Accept-Charset","utf-8")
                .build();


        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        JsonElement jsonElement = JsonParser.parseString(response.body());
        Film receivedFilm = gson.fromJson(jsonElement, Film.class);

        assertEquals(200, response.statusCode(), "Сервер вернул некорректный код состояния.");
        assertEquals(filmForTest, receivedFilm, "Создан некорректный фильм.");
    }

    @AfterEach
    void tearDown() {
        context.close();
    }
}
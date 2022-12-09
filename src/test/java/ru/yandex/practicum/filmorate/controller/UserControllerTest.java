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
import ru.yandex.practicum.filmorate.model.User;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
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

    //Тест для случая, когда в логине присутствует запрещенный символ - пробел
    @Test
    void checkUserValidationIfSpaceSymbolInLogin() {
        response = null;
        URI url = URI.create("http://localhost:8080/users");
        //Отправляем запрос с логином в котором содержится пробел
        User userForTest = new User(1, "user@yandex.ru", "my login", "Alex", LocalDate.of(1985, 11,10));

        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(gson.toJson(userForTest));
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

    //Тест для случая, когда отсутствует логин
    @Test
    void checkUserValidationIfNoLogin() {
        response = null;
        URI url = URI.create("http://localhost:8080/users");
        //Отправляем запрос с логином в котором содержится пробел
        User userForTest = new User(1, "user@yandex.ru", "", "Alex", LocalDate.of(1985, 11,10));

        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(gson.toJson(userForTest));
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

    //Тест для случая, когда логин указан корректно
    @Test
    void checkUserValidationIfNoSpaceSymbolInLogin() {
        response = null;
        URI url = URI.create("http://localhost:8080/users");
        //Отправляем запрос с логином в котором содержится пробел
        User userForTest = new User(1, "user@yandex.ru", "mylogin", "Alex", LocalDate.of(1985, 11,10));

        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(gson.toJson(userForTest));
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
        User receivedUser = gson.fromJson(jsonElement, User.class);

        assertEquals(200, response.statusCode(), "Сервер вернул некорректный код состояния.");
        assertEquals(userForTest, receivedUser, "Пользователь создан некорректно.");
    }

    //Тест для случая, когда адрес электронной почты указан некорректно
    @Test
    void checkUserValidationIfIncorrectEmail() {
        response = null;
        URI url = URI.create("http://localhost:8080/users");
        //Отправляем запрос с логином в котором содержится пробел
        User userForTest = new User(1, "@us@er yan///dex.ru@", "mylogin", "Alex", LocalDate.of(1985, 11,10));

        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(gson.toJson(userForTest));
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

    //Тест для случая, когда адрес электронной почты не указан
    @Test
    void checkUserValidationIfNoEmail() {
        response = null;
        URI url = URI.create("http://localhost:8080/users");
        //Отправляем запрос с логином в котором содержится пробел
        User userForTest = new User(1, "", "mylogin", "Alex", LocalDate.of(1985, 11,10));

        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(gson.toJson(userForTest));
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

    //Тест для случая, когда адрес электронной почты указан корректно
    @Test
    void checkUserValidationIfCorrectEmail() {
        response = null;
        URI url = URI.create("http://localhost:8080/users");
        //Отправляем запрос с логином в котором содержится пробел
        User userForTest = new User(1, "user@yandex.ru", "mylogin", "Alex", LocalDate.of(1985, 11,10));

        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(gson.toJson(userForTest));
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
        User receivedUser = gson.fromJson(jsonElement, User.class);

        assertEquals(200, response.statusCode(), "Сервер вернул некорректный код состояния.");
        assertEquals(userForTest, receivedUser, "Пользователь создан некорректно.");
    }

    //Тест для случая, когда не указано имя пользователя и в качестве имени должен быть логин пользователя
    @Test
    void checkUserValidationIfNameIsEmptyAndLoginUsedAsName() {
        response = null;
        URI url = URI.create("http://localhost:8080/users");
        //Отправляем запрос с логином в котором отсутствует имя пользователя
        User userForTest = new User(1, "user@yandex.ru", "mylogin", "", LocalDate.of(1985, 11,10));

        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(gson.toJson(userForTest));
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
        User receivedUser = gson.fromJson(jsonElement, User.class);

        //Проверяем код состояния и то, что у созданного пользователя имя равно логину указанному в запросе на создание пользователя
        assertEquals(200, response.statusCode(), "Сервер вернул некорректный код состояния");
        assertEquals(userForTest.getLogin(), receivedUser.getName(), "Сервер вернул некорректный код состояния");
    }

    //Тест для случая, когда дата рождения пользователя в будущем
    @Test
    void checkUserValidationIfBirthdayIsInTheFuture() {
        response = null;
        URI url = URI.create("http://localhost:8080/users");
        //Отправляем запрос с логином в котором содержится пробел
        User userForTest = new User(1, "user@yandex.ru", "mylogin", "Alex", LocalDate.of(2985, 11,10));

        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(gson.toJson(userForTest));
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

    //Тест для случая, когда дата рождения пользователя пустая
    @Test
    void checkUserValidationIfBirthdayIsEmpty() {
        response = null;
        URI url = URI.create("http://localhost:8080/users");
        //Отправляем запрос с логином в котором содержится пробел
        User userForTest = new User(1, "user@yandex.ru", "mylogin", "Alex", null);

        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(gson.toJson(userForTest));
        HttpRequest request = HttpRequest.newBuilder()
                .POST(body)
                .uri(url)
                .header("Content-Type", "application/json")
                .header("Accept-Charset", "utf-8")
                .build();


        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        //Проверяем, что сервер ответил 400 - Bad Request
        assertEquals(400, response.statusCode(), "Сервер вернул некорректный код состояния");
    }

    //Тест для случая, когда дата рождения пользователя корректна
    @Test
    void checkUserValidationIfBirthdayIsInThePast() {
        response = null;
        URI url = URI.create("http://localhost:8080/users");
        //Отправляем запрос с логином в котором содержится пробел
        User userForTest = new User(1, "user@yandex.ru", "mylogin", "Alex", LocalDate.of(1980, 11,10));

        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(gson.toJson(userForTest));
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
        User receivedUser = gson.fromJson(jsonElement, User.class);

        assertEquals(200, response.statusCode(), "Сервер вернул некорректный код состояния.");
        assertEquals(userForTest, receivedUser, "Пользователь создан некорректно.");
    }

    @AfterEach
    void tearDown() {
        context.close();
    }
}
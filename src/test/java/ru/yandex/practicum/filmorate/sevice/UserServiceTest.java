package ru.yandex.practicum.filmorate.sevice;

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
import ru.yandex.practicum.filmorate.controller.DateAdapter;
import ru.yandex.practicum.filmorate.model.User;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserServiceTest {
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

    @AfterEach
    void tearDown() {
        context.close();
    }

    @Test
    void checkCommonFriendsIfNoCommonFriends() {
        response = null;
        URI url = URI.create("http://localhost:8080/users");
        //Отправляем запрос с логином в котором содержится пробел
        User userForTest1 = new User(1, "user1@yandex.ru", "mylogin1", "Alex", LocalDate.of(1980, 11,10));
        User userForTest2 = new User(2, "user2@yandex.ru", "mylogin2", "Maxim", LocalDate.of(1979, 12,01));
        //Создаем пользователей
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(gson.toJson(userForTest1));
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

        body = HttpRequest.BodyPublishers.ofString(gson.toJson(userForTest2));
        request = HttpRequest.newBuilder()
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

        //получаем списко друзей пользователя
        url = URI.create("http://localhost:8080/users/1/friends");
        request = HttpRequest.newBuilder()
                .GET()
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
        List<User> receivedUsers = gson.fromJson(jsonElement, List.class);

        assertEquals(200, response.statusCode(), "Сервер вернул некорректный код состояния.");
        assertEquals(0, receivedUsers.size(), "Некорректное количество пользователей.");

        //Выясняем есть ли у них общие друзья
        url = URI.create("http://localhost:8080/users/1/friends/common/2");
        request = HttpRequest.newBuilder()
                .GET()
                .uri(url)
                .header("Content-Type","application/json")
                .header("Accept-Charset","utf-8")
                .build();

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }

        jsonElement = JsonParser.parseString(response.body());
        receivedUsers = gson.fromJson(jsonElement, List.class);

        assertEquals(200, response.statusCode(), "Сервер вернул некорректный код состояния.");
        assertEquals(0, receivedUsers.size(), "Некорректный список общих друзей.");
    }
}

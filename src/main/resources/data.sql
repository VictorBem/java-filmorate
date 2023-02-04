--создаю общие мастер данные - статусы дружбы, жанры, рейтинги,
INSERT INTO FRIENDSHIP_STATUS (name)
VALUES ('неподтверждённая'),
       ('подтверждённая');

INSERT INTO GENRE (name)
VALUES ('Комедия'),
       ('Драма'),
       ('Мультфильм'),
       ('Триллер'),
       ('Документальный'),
       ('Боевик');

INSERT INTO RATING (name)
VALUES ('G'),
       ('PG'),
       ('PG-13'),
       ('R'),
       ('NC-17');

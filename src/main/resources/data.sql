INSERT INTO users (name, login, email, birthday) VALUES
('Jzz', 'jzz123', 'jzz@example.com', '1992-12-01 00:00:00'),
('kll', 'kll456', 'kll@example.com', '1991-11-15 00:00:00'),
('KZ', 'KZ', 'kz@example.com', '1999-09-20 00:00:00');

INSERT INTO ratingMPA (name) VALUES
('G'),
('PG'),
('PG-13'),
('R'),
('NC-17');

INSERT INTO genres (name) VALUES
('Комедия'),
('Драма'),
('Мультфильм'),
('Триллер'),
('Документальный'),
('Боевик');

-- Добавление фильмов
INSERT INTO films (title, description, duration, ratingMPA_id, release_date) VALUES
('Inception', 'A mind-bending thriller', 148, 3, '2010-07-16 00:00:00'),
('The Matrix', 'A sci-fi classic', 136, 4, '1999-03-31 00:00:00');

-- Добавление друзей
INSERT INTO friends (user_id, friend_id) VALUES
(1, 2),
(2, 1),
(1, 3),
(3, 1),
(2, 3),
(3, 2);

-- Добавление лайков
INSERT INTO likes (user_id, film_id) VALUES
(1, 1),
(2, 1),
(3, 2);

INSERT INTO films_genre (film_id, genre_id) VALUES
(1, 4),
(2, 6);
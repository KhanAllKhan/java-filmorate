-- Удаление существующих таблиц (если нужно)
DROP TABLE IF EXISTS film_genres;
DROP TABLE IF EXISTS genres;
DROP TABLE IF EXISTS likes;
DROP TABLE IF EXISTS friends;
DROP TABLE IF EXISTS films;
DROP TABLE IF EXISTS users;

-- Таблица пользователей
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    login VARCHAR(255) NOT NULL,
    name VARCHAR(255),
    birthday DATE
);

-- Таблица фильмов
CREATE TABLE IF NOT EXISTS films (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    release_date DATE NOT NULL,
    duration INT NOT NULL,
    mpa_rating VARCHAR(10) NOT NULL
);

-- Таблица жанров
CREATE TABLE IF NOT EXISTS genres (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

-- Таблица для связи фильмов и жанров (многие-ко-многим)
CREATE TABLE IF NOT EXISTS film_genres (
    film_id BIGINT NOT NULL,
    genre_id BIGINT NOT NULL,
    FOREIGN KEY (film_id) REFERENCES films(id),
    FOREIGN KEY (genre_id) REFERENCES genres(id)
);

-- Таблица друзей
CREATE TABLE IF NOT EXISTS friends (
    user_id BIGINT NOT NULL,
    friend_id BIGINT NOT NULL,
    status VARCHAR(10),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (friend_id) REFERENCES users(id)
);

-- Таблица лайков
CREATE TABLE IF NOT EXISTS likes (
    film_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (film_id) REFERENCES films(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

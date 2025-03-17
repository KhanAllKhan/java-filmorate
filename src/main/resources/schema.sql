DROP TABLE IF EXISTS films_genre;
DROP TABLE IF EXISTS friends;
DROP TABLE IF EXISTS likes;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS films;
DROP TABLE IF EXISTS genres;
DROP TABLE IF EXISTS ratingMPA;

CREATE TABLE IF NOT EXISTS genres (
    genre_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS ratingMPA (
    ratingMPA_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL
);

-- Таблица пользователей
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    login VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    birthday TIMESTAMP NOT NULL
);

-- Таблица друзей (связь "один ко многим" с users)
CREATE TABLE IF NOT EXISTS friends (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    friend_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE, -- Связь с таблицей users
    FOREIGN KEY (friend_id) REFERENCES users(id) ON DELETE CASCADE -- Связь с таблицей users
);

-- Таблица фильмов
CREATE TABLE IF NOT EXISTS films (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    duration INT NOT NULL,
    ratingMPA_id BIGINT,
    release_date TIMESTAMP NOT NULL,
    FOREIGN KEY (ratingMPA_id) REFERENCES ratingMPA(ratingMPA_id) ON DELETE CASCADE
);

-- Таблица лайков (связь "один ко многим" с users и films)
CREATE TABLE IF NOT EXISTS likes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    film_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE, -- Связь с таблицей users
    FOREIGN KEY (film_id) REFERENCES films(id) ON DELETE CASCADE  -- Связь с таблицей films
);

CREATE TABLE IF NOT EXISTS films_genre (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    film_id BIGINT,
    genre_id BIGINT,
    FOREIGN KEY (film_id) REFERENCES films(id) ON DELETE CASCADE,
    FOREIGN KEY (genre_id) REFERENCES genres(genre_id) ON DELETE CASCADE
);
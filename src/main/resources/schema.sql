DROP TABLE IF EXISTS films_genre;
DROP TABLE IF EXISTS likes;
DROP TABLE IF EXISTS friends;

DROP TABLE IF EXISTS films;
DROP TABLE IF EXISTS users;
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

CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    login VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL, -- Уникальное поле
    birthday TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS friends (
    user_id BIGINT,
    friend_id BIGINT,
    PRIMARY KEY (user_id, friend_id), -- Составной первичный ключ
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (friend_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS films (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    duration INT NOT NULL,
    ratingMPA_id BIGINT,
    release_date TIMESTAMP NOT NULL,
    FOREIGN KEY (ratingMPA_id) REFERENCES ratingMPA(ratingMPA_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS likes (
    user_id BIGINT,
    film_id BIGINT,
    PRIMARY KEY (user_id, film_id), -- Составной первичный ключ
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (film_id) REFERENCES films(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS films_genre (
    film_id BIGINT,
    genre_id BIGINT,
    PRIMARY KEY (film_id, genre_id), -- Составной первичный ключ
    FOREIGN KEY (film_id) REFERENCES films(id) ON DELETE CASCADE,
    FOREIGN KEY (genre_id) REFERENCES genres(genre_id) ON DELETE CASCADE
);
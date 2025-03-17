CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    login VARCHAR(255) NOT NULL,
    name VARCHAR(255),
    birthday DATE
);

CREATE TABLE IF NOT EXISTS films (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    release_date DATE NOT NULL,
    duration INT NOT NULL,
    mpa_rating VARCHAR(10) NOT NULL
);

CREATE TABLE IF NOT EXISTS friends (
    user_id BIGINT NOT NULL,
    friend_id BIGINT NOT NULL,
    status VARCHAR(10),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (friend_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS likes (
    film_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (film_id) REFERENCES films(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

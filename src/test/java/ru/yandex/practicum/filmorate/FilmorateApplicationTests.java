package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.HttpClientErrorException;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmorateApplicationTests {

    @Autowired
    private UserController userController;

    @Autowired
    private FilmController filmController;

    @Test
    void contextLoads() {
    }

    @Test
    void testCreateUser() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("testUser");
        user.setName("Test User");
        user.setBirthday(LocalDate.of(2025, 1, 8));

        User createdUser = userController.create(user);
        assertNotNull(createdUser);
        assertEquals(user.getEmail(), createdUser.getEmail());
        assertEquals(user.getBirthday(), createdUser.getBirthday());
    }

    @Test
    void testUpdateUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("update@example.com");
        user.setLogin("updateUser");
        user.setName("Update User");
        user.setBirthday(LocalDate.of(2025, 1, 8));

        userController.create(user);

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setEmail("updated@example.com");
        updatedUser.setLogin("updatedUser");
        updatedUser.setName("Updated User");
        updatedUser.setBirthday(LocalDate.of(2025, 1, 8));

        User result = userController.update(updatedUser);
        assertNotNull(result);
        assertEquals(updatedUser.getEmail(), result.getEmail());
        assertEquals(updatedUser.getBirthday(), result.getBirthday());
    }


    @Test
    void testCreateFilm() {
        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("Test Description");
        film.setReleaseDate(LocalDate.of(2025, 1, 8));
        film.setDuration(120);

        Film createdFilm = filmController.create(film);
        assertNotNull(createdFilm);
        assertEquals(film.getName(), createdFilm.getName());
    }


    @Test
    void testUpdateFilm() {
        Film film = new Film();
        film.setId(1L);
        film.setName("Update Film");
        film.setDescription("Update Description");
        film.setReleaseDate(LocalDate.of(2025, 1, 8));
        film.setDuration(120);

        filmController.create(film);

        Film updatedFilm = new Film();
        updatedFilm.setId(1L);
        updatedFilm.setName("Updated Film");
        updatedFilm.setDescription("Updated Description");
        updatedFilm.setReleaseDate(LocalDate.of(2025, 1, 8));
        updatedFilm.setDuration(130);

        Film result = filmController.update(updatedFilm);
        assertNotNull(result);
        assertEquals(updatedFilm.getName(), result.getName());
    }
}

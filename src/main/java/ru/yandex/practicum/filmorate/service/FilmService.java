package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Collection<Film> getAllFilms() {
        log.info("Запрос на получение всех фильмов");
        return filmStorage.getAllFilms();
    }

    public Film getFilm(Long filmId) {
        log.info("Запрос на получение фильма с id={}", filmId);
        return filmStorage.getFilmById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм с id = " + filmId + " не найден"));
    }

    public List<Film> findPopularFilms(int count) {
        return filmStorage.getAllFilms().stream()
                .sorted(Comparator.comparing(Film::getLikesCount).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }


    public Film addFilm(Film film) {
        log.info("Запрос на добавление фильма: {}", film);
        return filmStorage.addFilm(film);
    }

    public Film addLike(Long filmId, Long userId) {
        log.info("Пользователь с id={} ставит лайк фильму с id={}", userId, filmId);

        // Проверяем существование пользователя
        User user = userStorage.getUserById(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }

        // Проверяем существование фильма
        Film film = filmStorage.getFilmById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм с id = " + filmId + " не найден"));

        film.getLikes().add(userId);
        filmStorage.updateFilm(film);
        filmStorage.addLike(filmId, userId);

        log.info("Лайк успешно добавлен для фильма с id={}", filmId);
        return film;
    }


    public Film updateFilm(Film newFilm) {
        log.info("Запрос на обновление фильма: {}", newFilm);
        return filmStorage.updateFilm(newFilm);
    }

    public Film removeLike(Long filmId, Long userId) {
        log.info("Пользователь с id={} удаляет лайк с фильма с id={}", userId, filmId);

        // Проверяем существование пользователя
        User user = userStorage.getUserById(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }

        // Проверяем существование фильма
        Film film = filmStorage.getFilmById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм с id = " + filmId + " не найден"));

        film.getLikes().remove(userId);
        filmStorage.updateFilm(film);
        filmStorage.deleteLike(filmId, userId);

        log.info("Лайк успешно удалён для фильма с id={}", filmId);
        return film;
    }

}
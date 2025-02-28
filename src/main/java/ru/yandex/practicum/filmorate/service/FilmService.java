package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    @Qualifier("FilmDbStorage")
    private final FilmStorage filmStorage;

    public Film create(Film film) {
        log.info("Создание нового фильма: {}", film);
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        log.info("Обновление фильма с id={}: {}", film.getId(), film);
        if (!filmStorage.findById(film.getId()).isPresent()) {
            throw new NotFoundException("Фильм с id = " + film.getId() + " не найден");
        }
        return filmStorage.update(film);
    }

    public List<Film> getAll() {
        log.info("Получение списка всех фильмов");
        return filmStorage.getAll();
    }

    public Film findById(Long id) {
        log.info("Получение фильма с id={}", id);
        return filmStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Фильм с id = " + id + " не найден"));
    }

    public void addLike(Long filmId, Long userId) {
        log.info("Пользователь {} ставит лайк фильму {}", userId, filmId);
        filmStorage.addLike(filmId, userId);
    }

    public void removeLike(Long filmId, Long userId) {
        log.info("Пользователь {} удаляет лайк с фильма {}", userId, filmId);
        filmStorage.removeLike(filmId, userId);
    }

    public List<Film> getPopularFilms(int count) {
        log.info("Получение {} популярных фильмов", count);
        return filmStorage.getPopularFilms(count);
    }
}

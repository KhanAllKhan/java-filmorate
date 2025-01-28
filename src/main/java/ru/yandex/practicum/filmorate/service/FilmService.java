package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;

    private static final LocalDate MINIMUM_DATE = LocalDate.of(1895, 12, 28);

    public Film create(Film film) {
        validateReleaseDate(film.getReleaseDate());
        film.setId(getNextId());
        return filmStorage.create(film);
    }

    public Film update(Film newFilm) {
        validateReleaseDate(newFilm.getReleaseDate());
        return filmStorage.update(newFilm);
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film findById(Long id) {
        return filmStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Фильм с id = " + id + " не найден"));
    }

    public void addLike(Long filmId, Long userId) {
        Film film = getFilmById(filmId);
        film.getLikes().add(userId);
    }

    public void removeLike(Long filmId, Long userId) {
        Film film = getFilmById(filmId);
        if (!film.getLikes().contains(userId)) {
            throw new NotFoundException("Лайк от пользователя с id = " + userId + " для фильма с id = " + filmId + " не найден");
        }
        film.getLikes().remove(userId);
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getAll().stream()
                .sorted((f1, f2) -> Integer.compare(f2.getLikes().size(), f1.getLikes().size()))
                .limit(count)
                .toList();
    }

    private Film getFilmById(Long filmId) {
        return filmStorage.findById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм с id = " + filmId + " не найден"));
    }

    private long getNextId() {
        long currentMaxId = filmStorage.getAll().stream()
                .mapToLong(Film::getId)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private void validateReleaseDate(LocalDate releaseDate) {
        if (releaseDate == null || releaseDate.isBefore(MINIMUM_DATE)) {
            throw new ConditionsNotMetException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }
    }
}

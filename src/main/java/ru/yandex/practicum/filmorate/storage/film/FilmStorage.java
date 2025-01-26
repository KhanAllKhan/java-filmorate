package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;


public interface FilmStorage {
    Film create(Film film);
    Film update(Film newFilm);
    List<Film> getAll();
    Optional<Film> findById(Long id);
    void addLike(Long filmId, Long userId);
    void removeLike(Long filmId, Long userId);
    List<Film> getPopularFilms(int count);
}

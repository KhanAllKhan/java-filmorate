package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    List<Film> getAllFilms();

    Film addFilm(Film film);

    Film updateFilm(Film newFilm);

    void addLike(Long filmId, Long userId);

    void deleteLike(Long filmId, Long userId);

    Optional<Film> getFilmById(Long filmId);

}
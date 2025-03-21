package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public ResponseEntity<Collection<Film>> getAllFilms() {
        log.info("Получен запрос на получение всех фильмов");
        return ResponseEntity.ok(filmService.getAllFilms());
    }

    @GetMapping("/{filmId}")
    public ResponseEntity<Film> getFilmById(@PathVariable Long filmId) {
        log.info("Получен запрос на получение фильма с id={}", filmId);
        return ResponseEntity.ok(filmService.getFilm(filmId));
    }

    @PostMapping
    public ResponseEntity<Film> createFilm(@RequestBody @Valid Film film) {
        log.info("Получен запрос на создание нового фильма: {}", film);
        return ResponseEntity.ok(filmService.addFilm(film));
    }

    @PutMapping
    public ResponseEntity<Film> updateFilm(@RequestBody @Valid Film film) {
        log.info("Получен запрос на обновление фильма: {}", film);
        return ResponseEntity.ok(filmService.updateFilm(film));
    }

    @PutMapping("/{filmId}/like/{userId}")
    public ResponseEntity<Void> addLike(@PathVariable Long filmId, @PathVariable Long userId) {
        log.info("Пользователь с id={} ставит лайк фильму с id={}", userId, filmId);
        filmService.addLike(filmId, userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public ResponseEntity<Void> removeLike(@PathVariable Long filmId, @PathVariable Long userId) {
        log.info("Пользователь с id={} удаляет лайк с фильма с id={}", userId, filmId);
        filmService.removeLike(filmId, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/popular")
    public ResponseEntity<List<Film>> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("Получен запрос на получение {} популярных фильмов", count);
        return ResponseEntity.ok(filmService.findPopularFilms(count));
    }
}
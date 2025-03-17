package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {
    private final GenreService genreService;

    @GetMapping
    public List<Genre> getAllGenres() {
        log.info("Получен запрос на получение всех жанров");
        return genreService.getAllGenres();
    }

    @GetMapping("/{id}")
    public Genre getGenreById(@PathVariable Long id) {
        log.info("Получен запрос на получение жанра с id={}", id);
        return genreService.getGenreById(id);
    }
}

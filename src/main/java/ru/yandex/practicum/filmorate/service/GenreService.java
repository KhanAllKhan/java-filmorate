package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class GenreService {
    private final GenreDbStorage genreStorage;

    public Collection<Genre> getAllGenres() {
        log.info("Получаем все жанры фильмов");
        return genreStorage.getAllGenres();
    }

    public Genre getGenre(int genreId) {
        Optional<Genre> genre = genreStorage.getGenre(genreId);

        if (genre.isEmpty()) {
            throw new NotFoundException("Жанра с id = " + genreId + " не найдено");
        }

        log.info("Получаем жанр с id: " + genreId);

        return genre.get();
    }
}
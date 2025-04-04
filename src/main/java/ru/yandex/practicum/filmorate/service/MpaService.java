package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;

import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class MpaService {
    private final MpaDbStorage mpaStorage;

    public Collection<MpaRating> getAllMpaRatings() {
        log.info("Получаем все рейтинги MPA фильмов");
        return mpaStorage.getAllMpaRatings();
    }

    public MpaRating getMpaRating(int mpaId) {
        Optional<MpaRating> mpaRating = mpaStorage.getMpaRating(mpaId);

        if (mpaRating.isEmpty()) {
            throw new NotFoundException("MPA рейтинга с id = " + mpaId + " не найдено");
        }

        log.info("Получаем рейтинг MPA с id: " + mpaId);

        return mpaRating.get();
    }

}
package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.MpaStorage;


import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaStorage mpaStorage;

    public List<MpaRating> getAllMpaRatings() {
        return mpaStorage.getAllMpaRatings();
    }

    public MpaRating getMpaById(Long id) {
        return mpaStorage.getMpaById(id)
                .orElseThrow(() -> new NotFoundException("Рейтинг с id = " + id + " не найден"));
    }
}

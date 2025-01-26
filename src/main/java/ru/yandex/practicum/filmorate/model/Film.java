package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;

@Data
public class Film {
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private double duration;
    private final Set<Long> likes = new HashSet<>(); // Поле для лайков
}


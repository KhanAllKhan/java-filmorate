package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Film.
 */
@Data
public class Film {

    Long id;

    @NotNull(message = "Поле name не может быть пустым")
    @NotBlank(message = "Поле name не может быть пустым")
    String name;

    @NotBlank
    @Size(max = 200)
    String description;

    @NotNull(message = "Дата релиза не может быть пустой")
    @Past
    LocalDate releaseDate;

    @NotNull
    @Positive
    int duration;

    Set<Long> likes;

    private Set<Genre> genres = new HashSet<>();

    @NotNull
    private MpaRating mpa;

    public int getLikesCount() {
        if (likes == null || likes.isEmpty()) {
            return 0;
        }
        return likes.size();
    }

}
package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.validation.ValidReleaseDate;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    private Long id;

    @NotNull(message = "Название не может быть пустым")
    @Size(min = 1, message = "Название не может быть пустым")
    private String name;

    @Size(max = 200, message = "Описание не может быть больше 200 символов")
    private String description;

    @NotNull(message = "Дата релиза не может быть пустой")
    @ValidReleaseDate(message = "Дата релиза не может быть раньше 28 декабря 1895 года")
    private LocalDate releaseDate;

    @PositiveOrZero(message = "Продолжительность фильма должна быть положительным числом")
    private int duration;

    private Set<Long> likes = new HashSet<>();
    @NotNull(message = "Жанр не может быть пустым")
    private Set<Genre> genres = new HashSet<>();
    @NotNull(message = "Рейтинг не должен быть пустым")
    private MpaRating mpaRating;
}

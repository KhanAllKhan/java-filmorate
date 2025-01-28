package ru.yandex.practicum.filmorate.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Valid
public class Film {
    private Long id;

    @NotNull(message = "Название не может быть пустым")
    @Size(min = 1, message = "Название не может быть пустым")
    private String name;

    @Size(max = 200, message = "Описание не может быть больше 200 символов")
    private String description;

    @NotNull(message = "Дата релиза не может быть пустой")
    @PastOrPresent(message = "Дата релиза не может быть в будущем и не может быть раньше 28 декабря 1895 года")
    private LocalDate releaseDate;

    @PositiveOrZero(message = "Продолжительность фильма должна быть положительным числом")
    private int duration;

    private Set<Long> likes = new HashSet<>();
}

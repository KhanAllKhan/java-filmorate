package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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

    private MpaRating mpa; // Убрали аннотацию @NotNull

    public MpaRating getMpa() {
        return mpa == null ? new MpaRating(1L, "G") : mpa; // Значение по умолчанию: id=1, name="G"
    }

    public void setMpa(MpaRating mpa) {
        this.mpa = mpa;
    }

    public int getLikesCount() {
        if (likes == null || likes.isEmpty()) {
            return 0;
        }
        return likes.size();
    }

}
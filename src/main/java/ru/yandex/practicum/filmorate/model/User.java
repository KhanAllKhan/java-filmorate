package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;


import java.time.LocalDate;
import java.util.Set;

@Data
public class User {
    private Long id;

    @NotNull(message = "Электронная почта не может быть пустой")
    @Email(message = "Электронная почта должна быть корректной и содержать символ @")
    private String email;

    @NotNull(message = "Логин не может быть пустым")
    @Pattern(regexp = "\\S+", message = "Логин не может содержать пробелы")
    private String login;

    private String name;

    @NotNull(message = "Дата рождения не может быть пустой")
    @Past(message = "Дата рождения должна быть в прошлом")
    private LocalDate birthday;

    private Set<Long> friends;
}

package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.*;

@Data
@NoArgsConstructor
public class User {

    Long id;

    @NotNull(message = "Поле email не может быть пустым")
    @Email(message = "Значение в поле email не соответствует формату почты")
    String email;

    @NotNull(message = "Поле login не может быть пустым")
    @NotBlank(message = "Поле login не может быть пустым")
    String login;

    @NotNull(message = "Поле name не может быть пустым")
    @NotBlank(message = "Поле name не может быть пустым")
    String name;

    @NotNull(message = "Поле дня рождения не может быть пустым")
    @Past
    LocalDate birthday;

    Set<Long> friends = new HashSet<>();

    private Map<Long, FriendshipStatus> friendshipStatuses = new HashMap<>();

}


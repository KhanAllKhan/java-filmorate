package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
public class User {
    private Long id;

    @NotBlank(message = "Email не должен быть пустым")
    @Email(message = "Неверный формат email. Пожалуйста, укажите действительный адрес электронной почты.")
    private String email;

    @NotBlank(message = "Логин не должен быть пустым!")
    private String login;

    private String name;

    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;

    private final Set<Long> friends = new HashSet<>();
    private Map<Long, FriendshipStatus> friendshipStatuses = new HashMap<>();
}

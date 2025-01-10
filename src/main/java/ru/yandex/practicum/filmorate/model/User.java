package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class User {
    private Long id;
    @NotBlank(message = "Email не должен быть пустым")
    @Email(message = "Неверный формат email. Пожалуйста, укажите действительный адрес электронной почты.")
    private String email;
    @NotBlank(message = "Логин не должен быть пустым")
    private String login;
    private String name;
    private LocalDate birthday;
}

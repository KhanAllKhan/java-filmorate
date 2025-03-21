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


    String name;

    @NotNull(message = "Поле дня рождения не может быть пустым")
    @Past
    LocalDate birthday;

    Set<Long> friends = new HashSet<>();

    private Map<Long, FriendshipStatus> friendshipStatuses = new HashMap<>();

    public User(Long id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(email, user.email) && Objects.equals(login, user.login) && Objects.equals(name, user.name) && Objects.equals(birthday, user.birthday) && Objects.equals(friends, user.friends);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, login, name, birthday, friends);
    }

}
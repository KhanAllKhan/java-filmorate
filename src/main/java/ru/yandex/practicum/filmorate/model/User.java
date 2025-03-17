package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashMap;
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

    private final Map<Long, FriendshipStatus> friendshipStatuses = new HashMap<>();

    // Поле для статуса дружбы (опционально, используется только при получении друзей)
    private String status;

    public void addFriend(Long friendId, FriendshipStatus status) {
        friendshipStatuses.put(friendId, status);
    }

    public void updateFriendshipStatus(Long friendId, FriendshipStatus newStatus) {
        if (friendshipStatuses.containsKey(friendId)) {
            friendshipStatuses.put(friendId, newStatus);
        }
    }

    public void removeFriend(Long friendId) {
        friendshipStatuses.remove(friendId);
    }

    public FriendshipStatus getFriendshipStatus(Long friendId) {
        return friendshipStatuses.get(friendId);
    }

    public Set<Long> getAllFriends() {
        return friendshipStatuses.keySet();
    }

    public String getName() {
        return (name == null || name.isBlank()) ? login : name;
    }
}

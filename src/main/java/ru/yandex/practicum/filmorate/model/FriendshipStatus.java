package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class FriendshipStatus {
    private Long id;       // ID статуса
    private String name;   // Название статуса (например, "confirmed")

    public FriendshipStatus(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}

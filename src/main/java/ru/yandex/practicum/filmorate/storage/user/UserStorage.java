package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

public interface UserStorage {
    Collection<User> getAllUsers();

    List<User> findCommonFriends(Long userId, Long friendId);

    User addUser(User user);

    User updateUser(User newUser);

    User removeFriend(Long userId, Long friendId);

    User getUserById(Long userId);

}
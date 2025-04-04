package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public Collection<User> findFriends(Long userId) {
        return getUserById(userId).getFriends().stream()
                .map(this::getUserById)
                .collect(Collectors.toList());
    }

    public Collection<User> findCommonFriends(Long userId, Long friendId) {
        return userStorage.findCommonFriends(userId, friendId);
    }

    public User addUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return userStorage.addUser(user);
    }

    public User addFriend(Long userId, Long friendId) {
        if (userId.equals(friendId)) {
            throw new IllegalArgumentException("Нельзя добавить в друзья самого себя");
        }

        userStorage.getUserById(userId);
        userStorage.getUserById(friendId);

        User user = getUserById(userId);
        user.getFriends().add(friendId);

        log.info("user: " + user.getFriends());

        return userStorage.updateUser(user);
    }

    public User updateUser(User newUser) {
        return userStorage.updateUser(newUser);
    }

    public User deleteFriend(Long userId, Long friendId) {
        User user = getUserById(userId);
        user.getFriends().remove(friendId);

        User friend = getUserById(friendId);
        friend.getFriends().remove(userId);

        userStorage.removeFriend(userId, friendId);

        userStorage.updateUser(user);
        userStorage.updateUser(friend);

        return user;
    }

    private User getUserById(Long userId) {
        return userStorage.getUserById(userId);
    }

}
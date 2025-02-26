package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User newUser) {
        if (!userStorage.findById(newUser.getId()).isPresent()) {
            throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
        }
        return userStorage.update(newUser);
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User findById(Long id) {
        return userStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + id + " не найден"));
    }

    public void addFriend(Long userId, Long friendId) {
        if (userId.equals(friendId)) {
            throw new IllegalArgumentException("Невозможно добавить самого себя в друзья");
        }

        User user = getUserById(userId);
        User friend = getUserById(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
    }

    public void removeFriend(Long userId, Long friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
    }

    public List<User> getFriends(Long userId) {
        User user = getUserById(userId);
        return user.getFriends().stream()
                .map(this::getUserById)
                .toList();
    }

    public List<User> getCommonFriends(Long userId, Long otherUserId) {
        User user = getUserById(userId);
        User otherUser = getUserById(otherUserId);
        Set<Long> commonFriendsIds = user.getFriends();
        commonFriendsIds.retainAll(otherUser.getFriends());
        return commonFriendsIds.stream()
                .map(this::getUserById)
                .toList();
    }

    public void confirmFriend(Long userId, Long friendId) {
        userStorage.confirmFriend(userId, friendId);
    }

    private User getUserById(Long userId) {
        return userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден"));
    }
}

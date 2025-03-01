package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    @Qualifier("userDbStorage")
    private final UserStorage userStorage;

    public User create(User user) {
        log.info("Создание нового пользователя: {}", user);
        return userStorage.create(user);
    }

    public User update(User newUser) {
        log.info("Обновление данных пользователя с id={}: {}", newUser.getId(), newUser);
        if (!userStorage.findById(newUser.getId()).isPresent()) {
            throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
        }
        return userStorage.update(newUser);
    }

    public List<User> getAll() {
        log.info("Получение списка всех пользователей");
        return userStorage.getAll();
    }

    public User findById(Long id) {
        log.info("Получение пользователя с id={}", id);
        return userStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + id + " не найден"));
    }

    public void addFriend(Long userId, Long friendId) {
        log.info("Добавление в друзья: пользователь {} добавляет {}", userId, friendId);
        if (!userStorage.findById(userId).isPresent() || !userStorage.findById(friendId).isPresent()) {
            throw new NotFoundException("Один из пользователей не существует");
        }
        userStorage.addFriend(userId, friendId);
    }

    public void removeFriend(Long userId, Long friendId) {
        log.info("Удаление из друзей: пользователь {} удаляет {}", userId, friendId);
        userStorage.removeFriend(userId, friendId);
    }
    public List<User> getFriends(Long userId) {
        log.info("Получение списка друзей пользователя с id={}", userId);
        List<User> friends = userStorage.getFriends(userId);
        if (friends.isEmpty()) {
            throw new NotFoundException("У пользователя с id = " + userId + " нет друзей");
        }
        log.info("Количество найденных друзей: {}", friends.size());
        return friends;
    }

    public List<User> getCommonFriends(Long userId, Long otherUserId) {
        log.info("Получение общих друзей пользователей {} и {}", userId, otherUserId);
        return userStorage.getCommonFriends(userId, otherUserId);
    }

    public void confirmFriend(Long userId, Long friendId) {
        log.info("Подтверждение дружбы между пользователями {} и {}", userId, friendId);
        userStorage.confirmFriend(userId, friendId);
    }
}

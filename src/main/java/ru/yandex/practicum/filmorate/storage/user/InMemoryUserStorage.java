package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

//
@Slf4j
@RequiredArgsConstructor
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User create(User user) {
        log.info("Получен запрос на создание пользователя: {}", user);
        user.setId(getNextId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User newUser) {
        User oldUser = users.get(newUser.getId());
        if (oldUser == null) {
            throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
        }
        oldUser.setEmail(newUser.getEmail());
        oldUser.setLogin(newUser.getLogin());
        oldUser.setName(newUser.getName());
        oldUser.setBirthday(newUser.getBirthday());
        return oldUser;
    }

    @Override
    public List<User> getAll() {
        log.info("Получен запрос на получение списка всех пользователей");
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        if (userId.equals(friendId)) {
            throw new IllegalArgumentException("Невозможно добавить самого себя в друзья");
        }
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
        user.getFriendshipStatuses().put(friendId, FriendshipStatus.NO);
        friend.getFriendshipStatuses().put(userId, FriendshipStatus.NO);
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
        user.getFriendshipStatuses().remove(friendId);
        friend.getFriendshipStatuses().remove(userId);
    }

    @Override
    public List<User> getFriends(Long userId) {
        User user = getUserById(userId);
        return user.getFriends().stream()
                .map(this::getUserById)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getCommonFriends(Long userId, Long otherUserId) {
        User user = getUserById(userId);
        User otherUser = getUserById(otherUserId);
        Set<Long> commonFriendsIds = new HashSet<>(user.getFriends());
        commonFriendsIds.retainAll(otherUser.getFriends());
        return commonFriendsIds.stream()
                .map(this::getUserById)
                .collect(Collectors.toList());
    }

    @Override
    public void confirmFriend(Long userId, Long friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        if (user.getFriends().contains(friendId) && friend.getFriends().contains(userId)) {
            user.getFriendshipStatuses().put(friendId, FriendshipStatus.YES);
            friend.getFriendshipStatuses().put(userId, FriendshipStatus.YES);
        } else {
            throw new NotFoundException("Запрос на дружбу не найден");
        }
    }

    private User getUserById(Long userId) {
        return findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден"));
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}

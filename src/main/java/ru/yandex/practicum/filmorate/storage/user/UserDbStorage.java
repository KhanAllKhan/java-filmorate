package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.BaseRepository;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class UserDbStorage extends BaseRepository<User> implements UserStorage {
    private static final String GET_ALL_USERS = "SELECT * FROM users;";

    private static final String ADD_USER = "INSERT INTO users(name, login, email, birthday) " +
            "VALUES (?, ?, ?, ?);";
    private static final String UPDATE_USER = "UPDATE users SET name = ?, login = ?, email = ?, birthday = ? " +
            "WHERE id = ?";
    private static final String GET_USER_BY_ID = "SELECT * FROM users WHERE id = ?;";
    private static final String ADD_FRIENDS = "INSERT INTO friends(user_id, friend_id) " +
            "VALUES (?, ?);";

    private static final String DELETE_FRIEND = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?;";


    public UserDbStorage(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper, User.class);
    }

    public List<User> getAllUsers() {
        log.info("Вывод всех пользователей");
        return findMany(GET_ALL_USERS).stream()
                .peek(user -> {
                    user.setFriends(loadFriends(user.getId()));
                })
                .collect(Collectors.toList());
    }

    public Set<Long> loadFriends(Long userId) {
        String sql = "SELECT friend_id FROM friends WHERE user_id = ?";
        return new HashSet<>(jdbc.query(sql, new Object[]{userId}, (rs, rowNum) -> rs.getLong("friend_id")));
    }

    public List<User> findCommonFriends(Long userId, Long friendId) {
        log.info("Вывод общих друзей для userId: " + userId + " и friendId: " + friendId);
        User user1 = this.getUserById(userId);
        User user2 = this.getUserById(friendId);
        log.info("Информация о user1: " + user1);
        log.info("Информация о user2: " + user2);
        Set<Long> friends1 = user1.getFriends();
        Set<Long> friends2 = user2.getFriends();
        friends1.retainAll(friends2);

        return friends1.stream()
                .map(this::getUserById)
                .collect(Collectors.toList());
    }

    public User addUser(User user) {
        long id = insert(
                ADD_USER,
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                Timestamp.valueOf(user.getBirthday().atStartOfDay())
        );

        log.info("Был создан пользователь с id: " + id);

        user.setId(id);
        return user;
    }

    public User updateUser(User newUser) {
        try {
            int rowsUpdated = update(
                    UPDATE_USER,
                    newUser.getName(),
                    newUser.getLogin(),
                    newUser.getEmail(),
                    Timestamp.valueOf(newUser.getBirthday().atStartOfDay()),
                    newUser.getId()
            );

            if (rowsUpdated == 0) {
                throw new NotFoundException("Пользователя для обновления с id: " + newUser.getId() + " не найдено");
            }

            if (!newUser.getFriends().isEmpty()) {
                for (Long friendId : newUser.getFriends()) {
                    update(
                            ADD_FRIENDS,
                            newUser.getId(),
                            friendId
                    );
                }
            }

            log.info("Была обновлена информация о пользователе с id: " + newUser.getId() + ". Его информация: " + getUserById(newUser.getId()));

        } catch (ConditionsNotMetException ex) {
            log.error("Ошибка при обновлении пользователя с id {}: {}", newUser.getId(), ex.getMessage());
            throw new ConditionsNotMetException("Не удалось обновить данные пользователя");
        }

        Optional<User> user = findOne(GET_USER_BY_ID, newUser.getId());

        log.info("id: " + user.get().getId() + " get: " + user.get().getFriends());

        if (user.isPresent()) {
            return user.get();
        }

        throw new NotFoundException("Пользователь с id: " + newUser.getId() + " не найден");

    }

    public User removeFriend(Long userId, Long friendId) {
        boolean deleted = delete(DELETE_FRIEND, userId, friendId);

        if (deleted) {
            return getUserById(userId);
        }

        log.info("У пользователя с id: " + userId + " не получилось удалить друга с id: " + friendId);

        return getUserById(userId);
    }

    public User getUserById(Long userId) {
        log.info("Вывод пользователя с id: " + userId);
        Optional<User> user = findOne(GET_USER_BY_ID, userId);
        if (user.isPresent()) {
            User updatedUser = user.get();
            updatedUser.setFriends(loadFriends(user.get().getId()));
            log.info("User с id: " + updatedUser.getId() + " имеет список друзей: " + updatedUser.getFriends());
            return updatedUser;
        }
        throw new NotFoundException("Пользователя с id = " + userId + " не найдено");
    }

}
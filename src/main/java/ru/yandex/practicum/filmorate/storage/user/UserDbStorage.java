package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.UserWithStatusRowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User create(User user) {
        // Реализация создания пользователя в БД
        return user; // Пример
    }

    @Override
    public User update(User user) {
        // Реализация обновления пользователя в БД
        return user; // Пример
    }

    @Override
    public List<User> getAll() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, new UserWithStatusRowMapper());
    }

    @Override
    public Optional<User> findById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        List<User> users = jdbcTemplate.query(sql, new UserWithStatusRowMapper(), id);
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        String sql = "INSERT INTO friends (user_id, friend_id, status_id) VALUES (?, ?, 1)";
        jdbcTemplate.update(sql, userId, friendId); // Статус "requested"
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        String sql = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public List<User> getFriends(Long userId) {
        String sql = "SELECT u.id, u.email, u.login, u.name, u.birthday " +
                "FROM friends f " +
                "JOIN users u ON f.friend_id = u.id " +
                "WHERE f.user_id = ?";
        return jdbcTemplate.query(sql, new UserWithStatusRowMapper(), userId);
    }

    @Override
    public List<User> getCommonFriends(Long userId, Long otherUserId) {
        String sql = "SELECT u.id, u.email, u.login, u.name, u.birthday " +
                "FROM friends f1 " +
                "JOIN friends f2 ON f1.friend_id = f2.friend_id " +
                "JOIN users u ON f1.friend_id = u.id " +
                "WHERE f1.user_id = ? AND f2.user_id = ?";
        return jdbcTemplate.query(sql, new UserWithStatusRowMapper(), userId, otherUserId);
    }

    @Override
    public void confirmFriend(Long userId, Long friendId) {
        String sql = "UPDATE friends SET status_id = 2 WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sql, userId, friendId); // Статус "confirmed"
    }
}

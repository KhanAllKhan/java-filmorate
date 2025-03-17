package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.mapper.UserWithStatusRowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Component
public class UserDbStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Получение друзей пользователя со статусами дружбы.
     */
    public List<User> getFriendsWithStatus(Long userId) {
        String sql = "SELECT u.id, u.email, u.login, u.name, u.birthday, fs.name AS status " +
                "FROM friends f " +
                "JOIN users u ON f.friend_id = u.id " +
                "JOIN friendship_status fs ON f.status_id = fs.id " +
                "WHERE f.user_id = ?";
        return jdbcTemplate.query(sql, new UserWithStatusRowMapper(), userId);
    }

    /**
     * Добавление друга пользователю с указанным статусом дружбы.
     */
    public void addFriend(Long userId, Long friendId, Long statusId) {
        String sql = "INSERT INTO friends (user_id, friend_id, status_id) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, userId, friendId, statusId);
    }

    /**
     * Обновление статуса дружбы.
     */
    public void updateFriendshipStatus(Long userId, Long friendId, Long statusId) {
        String sql = "UPDATE friends SET status_id = ? WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sql, statusId, userId, friendId);
    }

    /**
     * Удаление друга.
     */
    public void removeFriend(Long userId, Long friendId) {
        String sql = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sql, userId, friendId);
    }
}

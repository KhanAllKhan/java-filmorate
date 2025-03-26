package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.BaseRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class UserDbStorage extends BaseRepository<User> implements UserStorage {
    private static final String GET_ALL_USERS = """
            SELECT u.*, f.friend_id
            FROM users u
            LEFT JOIN friends f ON u.id = f.user_id
            """;

    private static final String GET_USER_WITH_FRIENDS = """
            SELECT u.*, f.friend_id
            FROM users u
            LEFT JOIN friends f ON u.id = f.user_id
            WHERE u.id = ?
            """;

    private static final String ADD_USER = "INSERT INTO users(name, login, email, birthday) VALUES (?, ?, ?, ?);";
    private static final String UPDATE_USER = "UPDATE users SET name = ?, login = ?, email = ?, birthday = ? WHERE id = ?";
    private static final String GET_USER_BY_ID = "SELECT * FROM users WHERE id = ?;";
    private static final String ADD_FRIEND = "INSERT INTO friends(user_id, friend_id) VALUES (?, ?);";
    private static final String DELETE_FRIEND = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?;";

    public UserDbStorage(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper, User.class);
    }

    @Override
    public List<User> getAllUsers() {
        log.info("Запрос всех пользователей с друзьями");
        return jdbc.query(GET_ALL_USERS, this::mapUserWithFriends);
    }

    @Override
    public List<User> findCommonFriends(Long userId, Long friendId) {
        log.info("Поиск общих друзей для userId: {} и friendId: {}", userId, friendId);
        Set<Long> commonFriends = new HashSet<>(getUserFriends(userId));
        commonFriends.retainAll(getUserFriends(friendId));
        return commonFriends.stream().map(this::getUserById).collect(Collectors.toList());
    }

    @Override
    public User addUser(User user) {
        long id = insert(
                ADD_USER,
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                Timestamp.valueOf(user.getBirthday().atStartOfDay())
        );
        log.info("Создан пользователь с id: {}", id);
        user.setId(id);
        return user;
    }

    @Override
    public User updateUser(User newUser) {
        int rowsUpdated = update(
                UPDATE_USER,
                newUser.getName(),
                newUser.getLogin(),
                newUser.getEmail(),
                Timestamp.valueOf(newUser.getBirthday().atStartOfDay()),
                newUser.getId()
        );

        if (rowsUpdated == 0) {
            throw new NotFoundException("Пользователь с id: " + newUser.getId() + " не найден");
        }

        log.info("Обновлена информация о пользователе с id: {}", newUser.getId());
        return getUserById(newUser.getId());
    }

    @Override
    public User removeFriend(Long userId, Long friendId) {
        int deleted = jdbc.update(DELETE_FRIEND, userId, friendId);
        if (deleted == 0) {
            log.warn("Не удалось удалить друга: userId={}, friendId={}", userId, friendId);
        }
        return getUserById(userId);
    }

    @Override
    public User getUserById(Long userId) {
        List<User> users = jdbc.query(GET_USER_WITH_FRIENDS, this::mapUserWithFriends, userId);
        if (users.isEmpty()) {
            throw new NotFoundException("Пользователь с id=" + userId + " не найден");
        }
        return users.get(0);
    }

    private Set<Long> getUserFriends(Long userId) {
        return getUserById(userId).getFriends();
    }

    private User mapUserWithFriends(ResultSet rs, int rowNum) throws SQLException {
        Long userId = rs.getLong("id");

        // Если пользователь уже был обработан, возвращаем его
        if (usersCache.containsKey(userId)) {
            return usersCache.get(userId);
        }

        User user = new User();
        user.setId(userId);
        user.setName(rs.getString("name"));
        user.setLogin(rs.getString("login"));
        user.setEmail(rs.getString("email"));
        user.setBirthday(rs.getTimestamp("birthday").toLocalDateTime().toLocalDate());

        Set<Long> friends = new HashSet<>();
        do {
            Long friendId = rs.getLong("friend_id");
            if (friendId != 0) {
                friends.add(friendId);
            }
        } while (rs.next() && rs.getLong("id") == userId);

        // Откатываем ResultSet назад, если это не последняя запись
        if (!rs.isAfterLast() && rs.getLong("id") != userId) {
            rs.previous();
        }

        user.setFriends(friends);
        usersCache.put(userId, user);
        return user;
    }
    //для кеша
    private final Map<Long, User> usersCache = new HashMap<>();
}
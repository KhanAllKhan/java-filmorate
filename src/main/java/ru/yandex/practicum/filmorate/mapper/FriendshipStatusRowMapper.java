package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FriendshipStatusRowMapper implements RowMapper<FriendshipStatus> {
    @Override
    public FriendshipStatus mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new FriendshipStatus(
                rs.getLong("id"),       // ID статуса
                rs.getString("name")    // Название статуса
        );
    }
}

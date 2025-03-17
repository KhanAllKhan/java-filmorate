package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<MpaRating> getAllMpaRatings() {
        String sql = "SELECT * FROM mpa";
        return jdbcTemplate.query(sql, this::mapRowToMpa);
    }

    @Override
    public Optional<MpaRating> getMpaById(Long id) {
        String sql = "SELECT * FROM mpa WHERE id = ?";
        List<MpaRating> mpaRatings = jdbcTemplate.query(sql, this::mapRowToMpa, id);
        return mpaRatings.isEmpty() ? Optional.empty() : Optional.of(mpaRatings.get(0));
    }

    private MpaRating mapRowToMpa(ResultSet rs, int rowNum) throws SQLException {
        return MpaRating.valueOf(rs.getString("name").toUpperCase());
    }
}

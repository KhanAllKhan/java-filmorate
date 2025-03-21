package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.BaseRepository;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Repository
public class MpaDbStorage extends BaseRepository<MpaRating> implements MpaStorage {
    private static final String GET_ALL_MPA = "SELECT * FROM ratingMPA";
    private static final String GET_MPA_RATING = "SELECT * FROM ratingMPA WHERE ratingMPA_id = ?";

    public MpaDbStorage(JdbcTemplate jdbc, RowMapper<MpaRating> mapper) {
        super(jdbc, mapper, MpaRating.class);
    }

    public Collection<MpaRating> getAllMpaRatings() {
        log.info("Вывод всех рейтингов MPA");
        return findMany(GET_ALL_MPA);
    }

    public Optional<MpaRating> getMpaRating(int mpaId) {
        log.info("Вывод рейтинга MPA с id: " + mpaId);
        return findOne(GET_MPA_RATING, mpaId);
    }
}

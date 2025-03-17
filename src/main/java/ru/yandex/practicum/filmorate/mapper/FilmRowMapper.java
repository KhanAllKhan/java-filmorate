package ru.yandex.practicum.filmorate.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Component
public class FilmRowMapper implements RowMapper<Film> {
    @Autowired
    private MpaStorage mpaStorage;


    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getLong("id"));
        film.setName(rs.getString("title"));
        film.setDescription(rs.getString("description"));
        film.setDuration(rs.getInt("duration"));
        film.setReleaseDate(rs.getTimestamp("release_date").toLocalDateTime().toLocalDate());

        Set<Long> likes = new HashSet<>();

        film.setLikes(likes);

        int mpaId = rs.getInt("ratingMPA_id");
        Optional<MpaRating> mpaRating = mpaStorage.getMpaRating(mpaId);
        if (mpaRating.isPresent()) {
            film.setMpa(mpaRating.get());
        } else {
            throw new NotFoundException("Рейтинга с id: " + mpaId + " не найдено");
        }

        return film;
    }
}
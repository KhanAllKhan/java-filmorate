package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FilmRowMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getLong("id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());
        film.setDuration(rs.getInt("duration"));

        // Создание объекта MpaRating
        film.setMpaRating(new MpaRating(
                rs.getLong("mpa_rating_id"),  // ID рейтинга из таблицы
                rs.getString("mpa_name")     // Название рейтинга из таблицы
        ));

        return film;
    }
}

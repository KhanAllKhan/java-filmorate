package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.BaseRepository;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class FilmDbStorage extends BaseRepository<Film> implements FilmStorage {
    private final GenreStorage genreStorage;

    // SQL queries
    private static final String GET_ALL_FILMS = """
        SELECT f.id, f.title, f.description, f.duration,
               f.release_date, f.ratingMPA_id
        FROM films AS f
        JOIN ratingMPA AS rm ON rm.ratingMPA_id = f.ratingMPA_id""";

    private static final String GET_FILM_BY_ID = """
        SELECT f.id, f.title, f.description, f.duration,
               f.release_date, f.ratingMPA_id
        FROM films AS f
        WHERE f.id = ?""";

    private static final String ADD_FILM = """
        INSERT INTO films(title, description, duration, release_date, ratingMPA_id)
        VALUES (?, ?, ?, ?, ?)""";

    private static final String ADD_GENRE = """
        INSERT INTO films_genre(film_id, genre_id)
        VALUES (?, ?)""";

    private static final String DELETE_GENRE = """
        DELETE FROM films_genre
        WHERE film_id = ?""";

    private static final String UPDATE_FILM = """
        UPDATE films
        SET title = ?, description = ?, duration = ?,
            release_date = ?, ratingMPA_id = ?
        WHERE id = ?""";

    private static final String GET_LIKES = """
        SELECT film_id, user_id FROM likes""";

    private static final String GET_LIKES_FOR_FILM = """
        SELECT user_id FROM likes WHERE film_id = ?""";

    private static final String ADD_LIKE = """
        INSERT INTO likes(user_id, film_id) VALUES (?, ?)""";

    private static final String REMOVE_LIKE = """
        DELETE FROM likes WHERE film_id = ? AND user_id = ?""";

    private static final String GET_GENRES_FOR_FILMS = """
        SELECT fg.film_id, g.genre_id, g.name
        FROM films_genre fg
        JOIN genres g ON fg.genre_id = g.genre_id""";

    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper, GenreStorage genreStorage) {
        super(jdbc, mapper, Film.class);
        this.genreStorage = genreStorage;
    }

    @Override
    public List<Film> getAllFilms() {
        log.info("Получение всех фильмов");

        Map<Long, Set<Genre>> genres = loadGenres(GET_GENRES_FOR_FILMS);
        Map<Long, Set<Long>> likes = loadLikes(GET_LIKES);

        return findMany(GET_ALL_FILMS).stream()
                .peek(film -> {
                    film.setLikes(likes.getOrDefault(film.getId(), Collections.emptySet()));
                    film.setGenres(genres.getOrDefault(film.getId(), Collections.emptySet()));
                })
                .collect(Collectors.toList());
    }

    @Override
    public Film addFilm(Film film) {
        validateFilm(film);

        long id = insert(
                ADD_FILM,
                film.getName(),
                film.getDescription(),
                film.getDuration(),
                Timestamp.valueOf(film.getReleaseDate().atStartOfDay()),
                film.getMpa().getId()
        );

        film.getGenres().forEach(genre ->
                insert(ADD_GENRE, id, genre.getId())
        );

        log.info("Создан фильм с id: {}", id);
        film.setId(id);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        validateFilm(film);

        int rowsUpdated = update(
                UPDATE_FILM,
                film.getName(),
                film.getDescription(),
                film.getDuration(),
                Timestamp.valueOf(film.getReleaseDate().atStartOfDay()),
                film.getMpa().getId(),
                film.getId()
        );

        if (rowsUpdated == 0) {
            throw new NotFoundException("Фильм с id: " + film.getId() + " не найден");
        }

        delete(DELETE_GENRE, film.getId());
        film.getGenres().forEach(genre ->
                insert(ADD_GENRE, film.getId(), genre.getId())
        );

        log.info("Обновлен фильм с id: {}", film.getId());
        return film;
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        int changedRows = jdbc.update(ADD_LIKE, userId, filmId);
        if (changedRows == 0) {
            throw new NotFoundException("Не удалось добавить лайк");
        }
        log.info("Добавлен лайк: filmId={}, userId={}", filmId, userId);
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        int changedRows = jdbc.update(REMOVE_LIKE, filmId, userId);
        if (changedRows == 0) {
            throw new NotFoundException("Не удалось удалить лайк");
        }
        log.info("Удален лайк: filmId={}, userId={}", filmId, userId);
    }

    @Override
    public Optional<Film> getFilmById(Long filmId) {
        Optional<Film> film = findOne(GET_FILM_BY_ID, filmId);
        film.ifPresent(f -> {
            f.setLikes(loadLikesForFilm(filmId));
            f.setGenres(new TreeSet<>(genreStorage.getFilmGenres(filmId)));
        });
        return film;
    }

    private Map<Long, Set<Long>> loadLikes(String sql) {
        return jdbc.query(sql, rs -> {
            Map<Long, Set<Long>> likesMap = new HashMap<>();
            while (rs.next()) {
                likesMap
                        .computeIfAbsent(rs.getLong("film_id"), k -> new HashSet<>())
                        .add(rs.getLong("user_id"));
            }
            return likesMap;
        });
    }

    private Set<Long> loadLikesForFilm(Long filmId) {
        return new HashSet<>(
                jdbc.query(GET_LIKES_FOR_FILM,
                        (rs, rowNum) -> rs.getLong("user_id"),
                        filmId)
        );
    }

    private Map<Long, Set<Genre>> loadGenres(String sql) {
        return jdbc.query(sql, rs -> {
            Map<Long, Set<Genre>> genreMap = new HashMap<>();
            while (rs.next()) {
                genreMap
                        .computeIfAbsent(rs.getLong("film_id"), k -> new TreeSet<>())
                        .add(new Genre(rs.getInt("genre_id"), rs.getString("name")));
            }
            return genreMap;
        });
    }

    private void validateFilm(Film film) {
        if (film.getReleaseDate().getYear() < 1895) {
            throw new ConditionsNotMetException("Дата релиза не может быть раньше 1895 года");
        }

        if (film.getMpa().getId() > 5) {
            throw new NotFoundException("Не найден рейтинг MPA с id: " + film.getMpa().getId());
        }

        film.getGenres().forEach(genre -> {
            if (genre.getId() > 6) {
                throw new NotFoundException("Не найден жанр с id: " + genre.getId());
            }
        });
    }
}
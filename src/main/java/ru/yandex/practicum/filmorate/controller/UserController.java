package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final Map<Long, User> posts = new HashMap<>();
    private static final LocalDate MINIMUM_DATE = LocalDate.of(2025, 1, 9);  // Минимальная допустимая дата

    @GetMapping
    public Collection<User> findAll() {
        log.info("Получен запрос на получение списка всех пользователей");
        return posts.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new NotFoundException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if (user.getLogin() == null || user.getLogin().contains(" ")) {
            throw new NotFoundException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            log.warn("Имя пользователя не указано, используется логин ({}) в качестве имени", user.getLogin());
            user.setName(user.getLogin());
        }
        if (user.getBirthday() == null || user.getBirthday().isAfter(MINIMUM_DATE)) {
            log.warn("Дата рождения не должна быть пустой и быть больше 09.01.25");
            throw new NotFoundException("Дата рождения не должна быть пустой и быть больше 09.01.25");
        }

        user.setId(user.getId());
        posts.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        if (newUser.getId() == null) {
            throw new NotFoundException("Id должен быть указан");
        }
        if (!posts.containsKey(newUser.getId())) {
            throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
        }
        if (newUser.getEmail() == null || !newUser.getEmail().contains("@")) {
            throw new NotFoundException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if (newUser.getLogin() == null || newUser.getLogin().contains(" ")) {
            throw new NotFoundException("Логин не может быть пустым и содержать пробелы");
        }
        if (newUser.getBirthday() == null || newUser.getBirthday().isAfter(MINIMUM_DATE)) {
            log.warn("Дата рождения не должна быть пустой и быть больше 09.01.25");
            throw new NotFoundException("Дата рождения не должна быть пустой и быть больше 09.01.25");
        }

        User oldUser = posts.get(newUser.getId());
        oldUser.setEmail(newUser.getEmail());
        oldUser.setLogin(newUser.getLogin());
        oldUser.setName(newUser.getName());
        oldUser.setBirthday(newUser.getBirthday());
        return oldUser;
    }

    private long getNextId() {
        long currentMaxId = posts.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}

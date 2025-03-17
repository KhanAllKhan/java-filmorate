package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MpaController {
    private final MpaService mpaService;

    @GetMapping
    public List<MpaRating> getAllMpaRatings() {
        return mpaService.getAllMpaRatings();
    }

    @GetMapping("/{id}")
    public MpaRating getMpaById(@PathVariable Long id) {
        return mpaService.getMpaById(id);
    }
}

package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Genre implements Comparable<Genre> {
    private Integer id;
    private String name;

    @Override
    public int compareTo(Genre other) {
        return this.getId() - other.getId();
    }
}
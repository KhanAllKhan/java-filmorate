package ru.yandex.practicum.filmorate.Handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConditionsNotMetException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConditionsNotMetException(ConditionsNotMetException ex) {
        return new ErrorResponse("Ошибка валидации: " + ex.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(NotFoundException ex) {
        return new ErrorResponse("Не найдено: " + ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception ex) {
        return new ErrorResponse("Внутренняя ошибка сервера: " + ex.getMessage());
    }

    private static class ErrorResponse {
        private final String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}

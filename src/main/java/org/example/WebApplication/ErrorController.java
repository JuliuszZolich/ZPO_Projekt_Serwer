package org.example.WebApplication;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * Klasa obsługująca wyjątki w aplikacji internetowej.
 */

@ControllerAdvice
public class ErrorController {

    /**
     * Obsługuje wyjątek NoHandlerFoundException i zwraca widok błędu 404.
     *
     * @return Nazwa widoku "error".
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound() {
        return "error";
    }
}
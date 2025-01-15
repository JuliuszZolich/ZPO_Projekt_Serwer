package org.example.WebApplication.Objects;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Error {
    private String error;
    private int error_code;

    public Error(String error, int error_code) {
        this.error = error;
        this.error_code = error_code;
    }

    public String toString() {
        return """
                {
                "error": "%s",
                "error_code": %d
                }
                """.formatted(error, error_code);
    }

}

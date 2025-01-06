package org.example.WebApplication;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.example.DatabaseUtils;
import org.example.WebApplication.Objects.Login;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.sql.ResultSet;
import java.sql.SQLException;

@Controller
public class LoginController {

    @GetMapping("/")
    public String login(Model model) {
        model.addAttribute("login", new Login());
        model.addAttribute("error", "");
        return "index";
    }
    @PostMapping("/")
    public String login(Login login, Model model, HttpServletResponse response) {
        String indeks = login.getIndeks();
        if (!indeks.matches("\\d{6}")) {
            model.addAttribute("error", "Niepoprawny indeks");
            return "index";
        }
        ResultSet student = DatabaseUtils.ExecuteQuery("SELECT * FROM Studenci WHERE indeks = " + indeks);
        try {
            if (student != null && student.next()) {
                Cookie cookie = new Cookie("indeks", indeks);
                response.addCookie(cookie);
                return "home";
            }
            model.addAttribute("error", "Student o podanym indeksie nie istnieje");
            return "index";
        } catch (SQLException e) {
            e.printStackTrace();
            model.addAttribute("error", "Błąd bazy danych");
            return "index";
        }
    }
}
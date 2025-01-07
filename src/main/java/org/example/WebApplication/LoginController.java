package org.example.WebApplication;

import org.example.DatabaseUtils;
import org.example.WebApplication.Objects.Login;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String login(Login login, Model model, RedirectAttributes redirectAttributes) {
        String indeks = login.getIndeks();
        if (!indeks.matches("\\d{6}")) {
            model.addAttribute("error", "Niepoprawny indeks");
            return "index";
        }
        ResultSet student = DatabaseUtils.ExecuteQuery("SELECT * FROM Studenci WHERE indeks = " + indeks);
        try {
            if (student != null && student.next()) {
                redirectAttributes.addAttribute("index", indeks);
                return "redirect:/home";
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
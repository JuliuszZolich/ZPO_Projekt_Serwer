package org.example.WebApplication;

import org.example.WebApplication.Objects.Login;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    @GetMapping("/")
    public String login(Model model) {
        model.addAttribute("login", new Login());
        return "index";
    }
    @PostMapping("/")
    public String login(Login login, Model model) {
        String indeks = login.getIndeks();
        if (!indeks.matches("\\d{6}")) {
            System.out.println("Niepoprawny indeks");
            model.addAttribute("error", "Niepoprawny indeks");
            return "index";
        }
        System.out.println("Indeks: " + login.getIndeks());
        model.addAttribute("indeks", login.ParseIndeks());
        return "home";
    }
}
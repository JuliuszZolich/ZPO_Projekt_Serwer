package org.example.WebApplication;

import jakarta.servlet.http.HttpServletRequest;
import org.example.Database.Repositories.StudentRepository;
import org.example.WebApplication.Objects.Login;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private StudentRepository studentRepository;

    @GetMapping("/")
    public String login(Model model, HttpServletRequest request) {
        logger.info("Logowanie z adresu: {}", request.getRemoteAddr());
        model.addAttribute("login", new Login());
        model.addAttribute("error", "");
        return "index";
    }
    @PostMapping("/")
    public String login(Login login, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        String indeks = login.getIndeks();
        if (!indeks.matches("\\d{6}")) {
            model.addAttribute("error", "Niepoprawny indeks");
            logger.warn("Niepoprawny indeks: {} z adresu: {}", indeks, request.getRemoteAddr());
            return "index";
        }
        try {
            if (studentRepository.existsById(Integer.valueOf(indeks))) {
                redirectAttributes.addAttribute("index", indeks);
                logger.info("Logowanie studenta o indeksie: {} z adresu: {}", indeks, request.getRemoteAddr());
                return "redirect:/home";
            }
            model.addAttribute("error", "Student o podanym indeksie nie istnieje");
            return "index";
        } catch (IllegalArgumentException e) {
            logger.error("Błąd bazy danych: {} z adresu: {} indeks: {}", e.getMessage(), request.getRemoteAddr(), indeks);
            model.addAttribute("error", "Błąd bazy danych");
            return "index";
        }
    }
}
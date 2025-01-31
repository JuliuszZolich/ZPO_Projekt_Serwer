package org.example.WebApplication;

import jakarta.servlet.http.HttpServletRequest;
import org.example.Database.Repositories.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Kontroler obsługujący żądania związane z logowaniem w aplikacji internetowej.
 */

@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private StudentRepository studentRepository;

    /**
     * Obsługuje żądanie GET dla strony logowania.
     *
     * @param model   Obiekt Model do przekazywania danych do widoku.
     * @param request Obiekt HttpServletRequest zawierający informacje o żądaniu HTTP.
     * @return Nazwa widoku "index".
     */
    @GetMapping("/")
    public String login(Model model, HttpServletRequest request) {
        logger.info("Logowanie z adresu: {}", request.getRemoteAddr());
        model.addAttribute("login", "");
        model.addAttribute("error", "");
        return "index";
    }

    /**
     * Obsługuje żądanie POST dla strony logowania.
     *
     * @param login               Indeks studenta.
     * @param model               Obiekt Model do przekazywania danych do widoku.
     * @param redirectAttributes  Atrybuty przekierowania do innego widoku.
     * @param request             Obiekt HttpServletRequest zawierający informacje o żądaniu HTTP.
     * @return Nazwa widoku "index" lub przekierowanie do "/home".
     */
    @PostMapping("/")
    public String login(@RequestParam String login, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        if (login == null) {
            model.addAttribute("error", "Niepoprawny indeks");
            logger.warn("Niepoprawny indeks z adresu: {}", request.getRemoteAddr());
            return "index";
        }
        if (!login.matches("\\d{6}")) {
            model.addAttribute("error", "Niepoprawny indeks");
            logger.warn("Niepoprawny indeks: {} z adresu: {}", login, request.getRemoteAddr());
            return "index";
        }
        try {
            if (studentRepository.existsById(Integer.valueOf(login))) {
                redirectAttributes.addAttribute("index", login);
                logger.info("Logowanie studenta o indeksie: {} z adresu: {}", login, request.getRemoteAddr());
                return "redirect:/home";
            }
            model.addAttribute("error", "Student o podanym indeksie nie istnieje");
            return "index";
        } catch (IllegalArgumentException e) {
            logger.error("Błąd bazy danych: {} z adresu: {} indeks: {}", e.getMessage(), request.getRemoteAddr(), login);
            model.addAttribute("error", "Błąd bazy danych");
            return "index";
        }
    }
}
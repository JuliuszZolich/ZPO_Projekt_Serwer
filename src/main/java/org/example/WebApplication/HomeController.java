package org.example.WebApplication;

import org.example.DatabaseUtils;
import org.example.WebApplication.Objects.Obecnosc;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String home(@ModelAttribute("index") String index, Model model) {

        // Pobierz dane terminów
        List<Obecnosc> obecnosci = new ArrayList<>();
        System.out.println("Index: " + index);
        try {
            ResultSet rs = DatabaseUtils.ExecuteQuery("SELECT nazwa, data, attendance FROM obecnosci JOIN terminy ON obecnosci.termin_id = terminy.id WHERE student_id = " + index);
            while (rs.next()) {
                Obecnosc o = new Obecnosc();
                o.setNazwa(rs.getString("nazwa"));
                o.setData(rs.getString("data"));
                o.setAttendance(rs.getInt("attendance"));
                obecnosci.add(o);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        model.addAttribute("obecnosci", obecnosci);
        return "home";
    }


}
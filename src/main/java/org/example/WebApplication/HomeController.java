package org.example.WebApplication;

import org.example.DatabaseUtils;
import org.example.WebApplication.Objects.Obecnosc;
import org.example.WebApplication.Objects.Termin;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String home() {

        // Pobierz dane terminów
        List<Termin> terminy = new ArrayList<>();
        try {
            ResultSet rs = DatabaseUtils.ExecuteQuery("SELECT * FROM terminy");
            while (rs.next()) {
                Termin termin = new Termin();
                termin.setId(rs.getInt("id"));
                termin.setGrupa_id(rs.getInt("grupa_id"));
                termin.setNazwa(rs.getString("nazwa"));
                termin.setData(rs.getDate("data"));
                termin.setProwadzacy_id(rs.getInt("prowadzacy_id"));
                terminy.add(termin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Pobierz dane obecności
        List<Obecnosc> obecnosci = new ArrayList<>();
        try {
            ResultSet rs = DatabaseUtils.ExecuteQuery("SELECT * FROM obecnosci");
            while (rs.next()) {
                Obecnosc obecnosc = new Obecnosc();
                obecnosc.setId(rs.getInt("id"));
                obecnosc.setStudent_id(rs.getInt("student_id"));
                obecnosc.setAttendance(rs.getInt("attendance"));
                obecnosc.setTermin_id(rs.getInt("termin_id"));
                obecnosci.add(obecnosc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "home";
    }


}
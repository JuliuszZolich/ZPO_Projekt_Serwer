package org.example.WebApplication;

import org.example.WebApplication.Objects.Student;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {
    @PostMapping("/api/dodajstudenta")
    public String AddStudent(@RequestBody Student student) {
        return student.getNazwisko();
    }
    @PostMapping("/api/usunstudenta")
    public String RemoveStudent() {
        return ":D";
    }
    @PostMapping("/api/dodajstudentagrupa")
    public String AddStudentFromGroup() {
        return ":D";
    }
    @PostMapping("/api/usunstudentagrupa")
    public String RemoveStudentFromGroup() {
        return ":D";
    }
    @PostMapping("/api/dodajtermin")
    public String AddTerm() {
        return ":D";
    }
    @GetMapping("/api/sprawdzobecnosc")
    public String CheckAttendance() {
        return ":D";
    }
    @GetMapping("/api/dziennik")
    public String CheckAttendanceList() {
        return ":D";
    }
}
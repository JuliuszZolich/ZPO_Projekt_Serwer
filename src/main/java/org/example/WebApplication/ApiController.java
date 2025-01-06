package org.example.WebApplication;

import org.example.DatabaseUtils;
import org.example.WebApplication.Objects.Student;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {
    @PostMapping("/api/dodajstudenta")
    public String AddStudent(@RequestBody Student student) {
        DatabaseUtils.ExecuteUpdate("INSERT INTO Studenci VALUES (" +
                student.getIndeks() + ", '" +
                student.getImie() + "', '" +
                student.getNazwisko() + "', " +
                student.getGrupa_id() + ", '" +
                student.getHaslo() + "')");
        return "";
    }

    @PostMapping("/api/login")
    public String Login() {
        return """
                {
                "id": 2137,
                "imie": "Jan",
                "nazwisko": "Kowalski",
                "haslo": ""
                }
                """;
    }

    @PostMapping("/api/usunstudenta")
    public String RemoveStudent(@RequestBody int id) {
        int ret = DatabaseUtils.ExecuteUpdate("DELETE FROM Studenci WHERE id = " + id);
        if (ret != 1) {
            return String.format("""
                    {
                    "error": "Nie ma takiego studenta",
                    "error_value": %d
                    }
                    """, id);
        }
        return """
                {
                "error": "",
                "error_value": 200
                }
                """;
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
        return """
                {"id": 2137}
                """;
    }
}
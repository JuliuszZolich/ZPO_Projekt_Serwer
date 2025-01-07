package org.example.WebApplication;

import org.example.DatabaseUtils;
import org.example.WebApplication.Objects.Prowadzacy;
import org.example.WebApplication.Objects.ProwadzacyLogin;
import org.example.WebApplication.Objects.Student;
import org.example.WebApplication.Objects.Termin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.sql.SQLException;

@RestController
public class ApiController {
    @PostMapping("/api/dodajstudenta")
    public String AddStudent(@RequestBody Student student) {
        int ret = DatabaseUtils.ExecuteUpdate("INSERT INTO Studenci VALUES (" +
                student.getIndeks() + ", '" +
                student.getImie() + "', '" +
                student.getNazwisko() + "', " +
                student.getGrupa_id() + ", '" +
                student.getHaslo() + "')");
        if (ret != 1) {
            return String.format("""
                    {
                    "error": "Nie udało się dodać studenta",
                    "error_value": %d,
                    "error_code: -1
                    }
                    """, student.getIndeks());
        }
        return """
                {
                "error": "",
                "error_value": 200,
                "error_code: 0
                }
                """;
    }

    @PostMapping("/api/login")
    public String Login(@RequestBody ProwadzacyLogin login) {
        ResultSet ret = DatabaseUtils.ExecuteQuery("SELECT * FROM Prowadzacy WHERE id = '" + login.getLogin() + "' AND haslo = '" + login.getPassword() + "'");
        try {
            if (ret == null || !ret.next()) {
                return """
                        {
                        "error": "Niepoprawne dane logowania",
                        "error_value": 401,
                        "error_code: -1
                        }
                        """;
            }
            Prowadzacy prowadzacy = new Prowadzacy();
            prowadzacy.setId(ret.getInt("id"));
            prowadzacy.setImie(ret.getString("imie"));
            prowadzacy.setNazwisko(ret.getString("nazwisko"));
            prowadzacy.setHaslo(ret.getString("haslo"));
            return """
                {
                "id": %d,
                "imie": "%s",
                "nazwisko": "%s",
                "haslo": "%s"
                }
                """.formatted(prowadzacy.getId(), prowadzacy.getImie(), prowadzacy.getNazwisko(), prowadzacy.getHaslo());
        }
        catch (SQLException e) {
            e.printStackTrace();
            return """
                    {
                    "error": "Błąd bazy danych",
                    "error_value": 500,
                    "error_code: -1
                    }
                    """;
        }

    }

    @PostMapping("/api/usunstudenta")
    public String RemoveStudent(@RequestBody int id) {
        int ret = DatabaseUtils.ExecuteUpdate("DELETE FROM Studenci WHERE id = " + id);
        if (ret != 1) {
            return String.format("""
                    {
                    "error": "Nie ma takiego studenta",
                    "error_value": %d,
                    "error_code: -1
                    }
                    """, id);
        }
        return """
                {
                "error": "",
                "error_value": 200,
                "error_code: 0
                }
                """;
    }

    @PostMapping("/api/dodajstudentagrupa")
    public String AddStudentToGroup(@RequestBody int student_id, @RequestBody int group_id) {
        int ret = DatabaseUtils.ExecuteUpdate("UPDATE Studenci SET grupa_id = " + group_id + " WHERE id = " + student_id);
        if (ret != 1) {
            return String.format("""
                    {
                    "error": "Nie ma takiego studenta",
                    "error_value": %d,
                    "error_code: -1
                    }
                    """, student_id);
        }
        return """
                {
                "error": "",
                "error_value": 200,
                "error_code: 0
                }
                """;
    }

    @PostMapping("/api/usunstudentagrupa")
    public String RemoveStudentFromGroup(@RequestBody int student_id) {
        int ret = DatabaseUtils.ExecuteUpdate("UPDATE Studenci SET grupa_id = NULL WHERE id = " + student_id);
        if (ret != 1) {
            return String.format("""
                    {
                    "error": "Nie ma takiego studenta",
                    "error_value": %d,
                    "error_code: -1
                    }
                    """, student_id);
        }
        return """
                {
                "error": "",
                "error_value": 200,
                "error_code: 0
                }
                """;
    }

    @PostMapping("/api/dodajtermin")
    public String AddTerm(@RequestBody Termin termin) {
        int ret = DatabaseUtils.ExecuteUpdate("INSERT INTO Terminy VALUES (" +
                termin.getId() + ", " +
                termin.getGrupa_id() + ", '" +
                termin.getNazwa() + "', '" +
                termin.getData() + "', " +
                termin.getProwadzacy_id() + ")");
        if (ret != 1) {
            return String.format("""
                    {
                    "error": "Nie udało się dodać terminu",
                    "error_value": %d,
                    "error_code: -1
                    }
                    """, termin.getId());
        }
        return """
                {
                "error": "",
                "error_value": 200,
                "error_code: 0
                }
                """;
    }

    @PostMapping("/api/usuntermin")
    public String RemoveTerm(@RequestBody int termin_id) {
        int ret = DatabaseUtils.ExecuteUpdate("DELETE FROM Terminy WHERE id = " + termin_id);
        if (ret != 1) {
            return String.format("""
                    {
                    "error": "Nie ma takiego terminu",
                    "error_value": %d,
                    "error_code: -1
                    }
                    """, termin_id);
        }
        return """
                {
                "error": "",
                "error_value": 200,
                "error_code: 0
                }
                """;
    }

    @GetMapping("/api/sprawdzobecnosc")
    public String CheckAttendance() {
        return ":D";
    }

    @GetMapping("/api/grupy")
    public String GetGroups() {
        ResultSet ret = DatabaseUtils.ExecuteQuery("SELECT * FROM Grupy");
        try {
            if (ret == null) {
                return """
                        {
                        "error": "Błąd bazy danych",
                        "error_value": 500,
                        "error_code: -1
                        }
                        """;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            while (ret.next()) {
                sb.append(String.format("""
                        {
                        "id": %d,
                        "nazwa": "%s"
                        },
                        """, ret.getInt("id"), ret.getString("nazwa")));
            }
            sb.deleteCharAt(sb.length()-2);
            sb.append("]");
            return sb.toString();
        }
        catch (SQLException e) {
            e.printStackTrace();
            return """
                    {
                    "error": "Błąd bazy danych",
                    "error_value": 500,
                    "error_code: -1
                    }
                    """;
        }
    }

    @GetMapping("/api/dziennik")
    public String CheckAttendanceList() {
        return """
                {"id": 2137}
                """;
    }
}
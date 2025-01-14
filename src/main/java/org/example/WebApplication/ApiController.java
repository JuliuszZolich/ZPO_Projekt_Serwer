package org.example.WebApplication;

import jakarta.servlet.http.HttpServletRequest;
import org.example.Database.Objects.Grupa;
import org.example.Database.Objects.Prowadzacy;
import org.example.Database.Objects.Student;
import org.example.Database.Objects.Termin;
import org.example.Database.Repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ApiController {

    Logger logger = LoggerFactory.getLogger(ApiController.class);

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private ProwadzacyRepository prowadzacyRepository;
    @Autowired
    private TerminRepository terminRepository;
    @Autowired
    private ObecnoscRepository obecnoscRepository;
    @Autowired
    private GrupaRepository grupaRepository;

    @GetMapping("/api/studenci")
    public List<Student> getStudents(HttpServletRequest request) {
        return studentRepository.findAll();
    }

    @PostMapping("/api/dodajstudenta")
    public String addStudent(@RequestParam int indeks, @RequestParam String imie, @RequestParam String nazwisko, HttpServletRequest request) {
        logger.info("Dodawanie studenta: {} {} {} z adresu : {}", indeks, imie, nazwisko, request.getRemoteAddr());
        Student student = new Student();
        student.setIndeks(indeks);
        student.setImie(imie);
        student.setNazwisko(nazwisko);
        student.setGrupaId(null);
        studentRepository.save(student);
        return """
                {
                "error": "",
                "error_code: 200
                }
                """;
    }

    @PostMapping("/api/login")
    public Prowadzacy login(@RequestParam int login, @RequestParam  String password, HttpServletRequest request) {
        logger.info("Logowanie prowadzacego: {} z adresu: {}", login, request.getRemoteAddr());
        try{
            return prowadzacyRepository.findByIdAndHaslo(login, password);
        } catch (Exception e) {
            logger.error("Błąd logowania prowadzacego: {} z adresu: {}", login, request.getRemoteAddr());
            return null;
        }

    }

    @PostMapping("/api/usunstudenta")
    public String removeStudent(@RequestParam int id, HttpServletRequest request) {
        logger.info("Usuwanie studenta o id: {} z adresu: {}", id, request.getRemoteAddr());
        studentRepository.deleteById(id);
        return """
                {
                "error": "",
                "error_code: 200
                }
                """;
    }

    @PostMapping("/api/dodajstudentagrupa")
    public String addStudentToGroup(@RequestParam int studentId, @RequestParam int groupId, HttpServletRequest request) {
        logger.info("Dodawanie studenta o id: {} do grupy o id: {} z adresu: {}", studentId, groupId, request.getRemoteAddr());
        if (studentRepository.findById(studentId).isEmpty()){
            logger.error("Nie ma takiego studenta o id: {} z adresu: {}", studentId, request.getRemoteAddr());
            return """
                    {
                    "error": "Nie ma takiego studenta",
                    "error_code: 404
                    }
                    """;
        }
        Student student = studentRepository.findById(studentId).get();
        student.setGrupaId(groupId);
        studentRepository.save(student);
        return """
                {
                "error": "",
                "error_code: 200
                }
                """;
    }

    @PostMapping("/api/usunstudentagrupa")
    public String removeStudentFromGroup(@RequestParam int studentId, HttpServletRequest request) {
        logger.info("Usuwanie studenta o id: {} z grupy z adresu: {}", studentId, request.getRemoteAddr());
        if (studentRepository.findById(studentId).isEmpty()) {
            logger.error("Nie ma takiego studenta o id: {} z adresu: {}", studentId, request.getRemoteAddr());
            return """
                    {
                    "error": "Nie ma takiego studenta",
                    "error_code": 404
                    }
                    """;
        }
        Student student = studentRepository.findById(studentId).get();
        student.setGrupaId(null);
        studentRepository.save(student);
        return """
                {
                "error": "",
                "error_code": 200
                }
                """;
    }

    @PostMapping("/api/dodajtermin")
    public String addTerm(@RequestParam int grupaId, @RequestParam String nazwa, @RequestParam String data, @RequestParam int prowadzacyId, HttpServletRequest request) {
        logger.info("Dodawanie terminu: {} {} {} {} z adresu: {}", grupaId, nazwa, data, prowadzacyId, request.getRemoteAddr());
        Termin termin = new Termin();
        termin.setGrupaId(grupaId);
        termin.setNazwa(nazwa);
        termin.setData(data);
        termin.setProwadzacyId(prowadzacyId);
        terminRepository.save(termin);
        return """
                {
                "error": "",
                "error_code: 200
                }
                """;
    }

    @PostMapping("/api/usuntermin")
    public String removeTerm(@RequestParam int terminId, HttpServletRequest request) {
        logger.info("Usuwanie terminu o id: {} z adresu: {}", terminId, request.getRemoteAddr());
        terminRepository.deleteById(terminId);
        return """
                {
                "error": "",
                "error_code: 200
                }
                """;
    }

    @GetMapping("/api/sprawdzobecnosc")
    public String checkAttendance(HttpServletRequest request) {
        return ":D";
    }

    @GetMapping("/api/grupy")
    public List<Grupa> getGroups(HttpServletRequest request) {
        logger.info("Pobieranie grup z adresu: {}", request.getRemoteAddr());
        return grupaRepository.findAll();
    }

    @PostMapping("/api/dodajgrupe")
    public String addGroup(@RequestParam String nazwa   , HttpServletRequest request) {
        logger.info("Dodawanie grupy: {} z adresu: {}", nazwa, request.getRemoteAddr());
        Grupa grupa = new Grupa();
        grupa.setNazwa(nazwa);
        try{
            grupaRepository.save(grupa);
            return """
                {
                "error": "",
                "error_code: 200
                }
                """;
        } catch (Exception e) {
            return """
                {
                "error": "Nie udało się dodać grupy",
                "error_code: 403
                }
                """;
        }
    }

    @PostMapping("/api/usungrupe")
    public String removeGroup(@RequestParam int grupaId, HttpServletRequest request) {
        logger.info("Usuwanie grupy o id: {} z adresu: {}", grupaId, request.getRemoteAddr());
        grupaRepository.deleteById(grupaId);
        return """
                {
                "error": "",
                "error_value": 200,
                "error_code: 0
                }
                """;
    }

    @GetMapping("/api/dziennik")
    public String checkAttendanceList(HttpServletRequest request) {
        return """
                {"id": 2137}
                """;
    }
}
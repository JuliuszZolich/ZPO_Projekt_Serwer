package org.example.WebApplication.Objects;

import jakarta.servlet.http.HttpServletRequest;
import org.example.Database.Objects.*;
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
@RequestMapping("/api")
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

    @GetMapping("/studenci")
    public List<Student> getStudents(HttpServletRequest request) {
        logger.info("Pobieranie studentów z adresu: {}", request.getRemoteAddr());
        return studentRepository.findAll();
    }

    @GetMapping("/studencigrupa")
    public List<Student> getStudentsFromGroup(@RequestParam int grupaId, HttpServletRequest request) {
        logger.info("Pobieranie studentów z grupy o id: {} z adresu: {}", grupaId, request.getRemoteAddr());
        return studentRepository.findByGrupaId(grupaId);
    }


    @PostMapping("/dodajstudenta")
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

    @PostMapping("/login")
    public Prowadzacy login(@RequestParam int login, @RequestParam  String password, HttpServletRequest request) {
        logger.info("Logowanie prowadzacego: {} z adresu: {}", login, request.getRemoteAddr());
        try{
            return prowadzacyRepository.findByIdAndHaslo(login, password);
        } catch (Exception e) {
            logger.error("Błąd logowania prowadzacego: {} z adresu: {}", login, request.getRemoteAddr());
            return null;
        }

    }

    @PostMapping("/usunstudenta")
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

    @PostMapping("/dodajstudentagrupa")
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

    @PostMapping("/usunstudentagrupa")
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

    @PostMapping("/dodajtermin")
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

    @PostMapping("/usuntermin")
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

    @GetMapping("/termingrupa")
    public List<Termin> getTermsFromGroup(@RequestParam int grupaId, HttpServletRequest request) {
        logger.info("Pobieranie terminów z grupy o id: {} z adresu: {}", grupaId, request.getRemoteAddr());
        return terminRepository.findByGrupaId(grupaId);
    }

    @GetMapping("/sprawdzobecnosc")
    public List<Obecnosc> checkAttendance(@RequestParam int terminId, HttpServletRequest request) {
        logger.info("Sprawdzanie obecności na terminie o id: {} z adresu: {}", terminId, request.getRemoteAddr());
        return obecnoscRepository.findByTerminId(terminId);
    }
    @GetMapping("/sprawdzobecnoscstudenta")
    public Obecnosc checkStudentAttendanceAtTerm(@RequestParam int studentId, @RequestParam int terminId, HttpServletRequest request) {
        logger.info("Sprawdzanie obecności studenta o id: {} na terminie o id: {} z adresu: {}", studentId, terminId, request.getRemoteAddr());
        return obecnoscRepository.findByStudentIdAndTerminId(studentId, terminId);
    }

    @GetMapping("/grupy")
    public List<Grupa> getGroups(HttpServletRequest request) {
        logger.info("Pobieranie grup z adresu: {}", request.getRemoteAddr());
        return grupaRepository.findAll();
    }

    @PostMapping("/dodajgrupe")
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

    @PostMapping("/usungrupe")
    public String removeGroup(@RequestParam int grupaId, HttpServletRequest request) {
        logger.info("Usuwanie grupy o id: {} z adresu: {}", grupaId, request.getRemoteAddr());
        grupaRepository.deleteById(grupaId);
        List<Student> students = studentRepository.findByGrupaId(grupaId);
        for (Student student : students) {
            student.setGrupaId(null);
            studentRepository.save(student);
        }
        return """
                {
                "error": "",
                "error_value": 200,
                "error_code: 0
                }
                """;
    }

    @GetMapping("/dziennik")
    public String checkAttendanceList(HttpServletRequest request) {
        return """
                {"id": 2137}
                """;
    }
}
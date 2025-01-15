package org.example.WebApplication;

import jakarta.servlet.http.HttpServletRequest;
import org.example.Database.Objects.*;
import org.example.Database.Objects.Prowadzacy;
import org.example.Database.Objects.Student;
import org.example.Database.Objects.Termin;
import org.example.Database.Repositories.*;
import org.example.WebApplication.Objects.Error;
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
        if(grupaRepository.findById(grupaId).isEmpty()){
            logger.error("Nie ma takiej grupy o id: {} z adresu: {}", grupaId, request.getRemoteAddr());
            return null;
        }
        logger.info("Pobieranie studentów z grupy o id: {} z adresu: {}", grupaId, request.getRemoteAddr());
        return studentRepository.findByGrupaId(grupaId);
    }


    @PostMapping("/dodajstudenta")
    public String addStudent(@RequestParam int indeks, @RequestParam String imie, @RequestParam String nazwisko, HttpServletRequest request) {
        if(studentRepository.existsStudentByIndeks(indeks)){
            logger.error("Student o indeksie: {} już istnieje z adresu: {}", indeks, request.getRemoteAddr());
            return new Error("Student o podanym indeksie już istnieje", 403).toString();
        }
        logger.info("Dodawanie studenta: {} {} {} z adresu : {}", indeks, imie, nazwisko, request.getRemoteAddr());
        Student student = new Student();
        student.setIndeks(indeks);
        student.setImie(imie);
        student.setNazwisko(nazwisko);
        student.setGrupaId(null);
        studentRepository.save(student);
        return new Error("", 200).toString();
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
        if (studentRepository.findById(id).isEmpty()){
            logger.error("Nie ma takiego studenta o id: {} z adresu: {}", id, request.getRemoteAddr());
            return new Error("Nie ma takiego studenta", 404).toString();
        }
        logger.info("Usuwanie studenta o id: {} z adresu: {}", id, request.getRemoteAddr());
        studentRepository.deleteById(id);
        obecnoscRepository.deleteAllByStudentId(id);
        return new Error("", 200).toString();
    }

    @PostMapping("/dodajstudentagrupa")
    public String addStudentToGroup(@RequestParam int studentId, @RequestParam int groupId, HttpServletRequest request) {
        logger.info("Dodawanie studenta o id: {} do grupy o id: {} z adresu: {}", studentId, groupId, request.getRemoteAddr());
        if (studentRepository.findById(studentId).isEmpty()){
            logger.error("Nie ma takiego studenta o id: {} z adresu: {}", studentId, request.getRemoteAddr());
            return new Error("Nie ma takiego studenta", 404).toString();
        }
        if (studentRepository.findById(studentId).get().getGrupaId() != null){
            logger.error("Student o id: {} jest już w grupie o id: {} z adresu: {}", studentId, studentRepository.findById(studentId).get().getGrupaId(), request.getRemoteAddr());
            return new Error("Student jest już w grupie", 403).toString();
        }
        Student student = studentRepository.findById(studentId).get();
        student.setGrupaId(groupId);
        studentRepository.save(student);
        for(Termin t: terminRepository.findByGrupaId(groupId)){
            Obecnosc obecnosc = new Obecnosc();
            obecnosc.setStudentId(studentId);
            obecnosc.setTerminId(t.getId());
            obecnosc.setAttendance(0);
            obecnoscRepository.save(obecnosc);
        }
        return new Error("", 200).toString();
    }

    @PostMapping("/usunstudentagrupa")
    public String removeStudentFromGroup(@RequestParam int studentId, HttpServletRequest request) {
        logger.info("Usuwanie studenta o id: {} z grupy z adresu: {}", studentId, request.getRemoteAddr());
        if (studentRepository.findById(studentId).isEmpty()) {
            logger.error("Nie ma takiego studenta o id: {} z adresu: {}", studentId, request.getRemoteAddr());
            return new Error("Nie ma takiego studenta", 404).toString();
        }
        Student student = studentRepository.findById(studentId).get();
        student.setGrupaId(null);
        studentRepository.save(student);
        obecnoscRepository.deleteAllByStudentId(studentId);
        return new Error("", 200).toString();
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
        for(Student s: studentRepository.findByGrupaId(grupaId)){
            Obecnosc obecnosc = new Obecnosc();
            obecnosc.setStudentId(s.getIndeks());
            obecnosc.setTerminId(termin.getId());
            obecnosc.setAttendance(0);
            obecnoscRepository.save(obecnosc);
        }
        return new Error("", 200).toString();
    }

    @PostMapping("/usuntermin")
    public String removeTerm(@RequestParam int terminId, HttpServletRequest request) {
        if(terminRepository.findById(terminId).isEmpty()){
            logger.error("Nie ma takiego terminu o id: {} z adresu: {}", terminId, request.getRemoteAddr());
            return new Error("Nie ma takiego terminu", 404).toString();
        }
        logger.info("Usuwanie terminu o id: {} z adresu: {}", terminId, request.getRemoteAddr());
        terminRepository.deleteById(terminId);
        obecnoscRepository.deleteAllByTerminId(terminId);
        return new Error("", 200).toString();
    }

    @GetMapping("/termingrupa")
    public List<Termin> getTermsFromGroup(@RequestParam int grupaId, HttpServletRequest request) {
        if(grupaRepository.findById(grupaId).isEmpty()){
            logger.error("Nie ma takiej grupy o id: {} z adresu: {}", grupaId, request.getRemoteAddr());
            return null;
        }
        logger.info("Pobieranie terminów z grupy o id: {} z adresu: {}", grupaId, request.getRemoteAddr());
        return terminRepository.findByGrupaId(grupaId);
    }

    @GetMapping("/sprawdzobecnosc")
    public List<Obecnosc> checkAttendance(@RequestParam int terminId, HttpServletRequest request) {
        if(terminRepository.findById(terminId).isEmpty()){
            logger.error("Nie ma takiego terminu o id: {} z adresu: {}", terminId, request.getRemoteAddr());
            return null;
        }
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
            return new Error("", 200).toString();
        } catch (Exception e) {
            return new Error("Nie udało się dodać grupy", 403).toString();
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
        List<Termin> terms = terminRepository.findByGrupaId(grupaId);
        for (Termin term : terms) {
            obecnoscRepository.deleteAllByTerminId(term.getId());
            terminRepository.deleteById(term.getId());
        }
        return new Error("", 200).toString();
    }

    @GetMapping("/dziennik")
    public String checkAttendanceList(HttpServletRequest request) {
        return """
                {"id": 2137}
                """;
    }
}
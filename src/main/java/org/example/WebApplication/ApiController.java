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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> getStudents(HttpServletRequest request) {
        logger.info("Pobieranie studentów z adresu: {}", request.getRemoteAddr());
        return ResponseEntity.status(HttpStatus.OK).body(studentRepository.findAll());
    }

    @GetMapping("/studencigrupa")
    public ResponseEntity<?> getStudentsFromGroup(@RequestParam int grupaId, HttpServletRequest request) {
        if(grupaRepository.findById(grupaId).isEmpty()){
            logger.error("Nie ma takiej grupy o id: {} z adresu: {}", grupaId, request.getRemoteAddr());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error("Nie ma takiej grupy"));
        }
        logger.info("Pobieranie studentów z grupy o id: {} z adresu: {}", grupaId, request.getRemoteAddr());
        return ResponseEntity.status(HttpStatus.OK).body(studentRepository.findByGrupaId(grupaId));
    }


    @PostMapping("/dodajstudenta")
    public ResponseEntity<?> addStudent(@RequestParam int indeks, @RequestParam String imie, @RequestParam String nazwisko, HttpServletRequest request) {
        if(studentRepository.existsStudentByIndeks(indeks)){
            logger.error("Student o indeksie: {} już istnieje z adresu: {}", indeks, request.getRemoteAddr());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new Error("Student o podanym indeksie już istnieje"));
        }
        logger.info("Dodawanie studenta: {} {} {} z adresu : {}", indeks, imie, nazwisko, request.getRemoteAddr());
        Student student = new Student();
        student.setIndeks(indeks);
        student.setImie(imie);
        student.setNazwisko(nazwisko);
        student.setGrupaId(null);
        studentRepository.save(student);
        return ResponseEntity.status(HttpStatus.OK).body(new Error(""));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam int login, @RequestParam  String password, HttpServletRequest request) {
        logger.info("Logowanie prowadzacego: {} z adresu: {}", login, request.getRemoteAddr());
        try{
            Prowadzacy prowadzacy = prowadzacyRepository.findByIdAndHaslo(login, password);
            if(prowadzacy == null){
                logger.error("Błąd logowania prowadzacego: {} z adresu: {}", login, request.getRemoteAddr());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Error("Błąd logowania"));
            }
            return ResponseEntity.status(HttpStatus.OK).body(prowadzacy);
        } catch (Exception e) {
            logger.error("Błąd logowania prowadzacego: {} z adresu: {}", login, request.getRemoteAddr());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Error("Błąd logowania"));
        }

    }

    @PostMapping("/usunstudenta")
    public ResponseEntity<?> removeStudent(@RequestParam int id, HttpServletRequest request) {
        if (studentRepository.findById(id).isEmpty()){
            logger.error("Nie ma takiego studenta o id: {} z adresu: {}", id, request.getRemoteAddr());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error("Nie ma takiego studenta"));
        }
        logger.info("Usuwanie studenta o id: {} z adresu: {}", id, request.getRemoteAddr());
        studentRepository.deleteById(id);
        obecnoscRepository.deleteAllByStudentId(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Error(""));
    }

    @PostMapping("/dodajstudentagrupa")
    public ResponseEntity<?> addStudentToGroup(@RequestParam int studentId, @RequestParam int groupId, HttpServletRequest request) {
        logger.info("Dodawanie studenta o id: {} do grupy o id: {} z adresu: {}", studentId, groupId, request.getRemoteAddr());
        if (studentRepository.findById(studentId).isEmpty()){
            logger.error("Nie ma takiego studenta o id: {} z adresu: {}", studentId, request.getRemoteAddr());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error("Nie ma takiego studenta"));
        }
        if (studentRepository.findById(studentId).get().getGrupaId() != null){
            logger.error("Student o id: {} jest już w grupie o id: {} z adresu: {}", studentId, studentRepository.findById(studentId).get().getGrupaId(), request.getRemoteAddr());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new Error("Student jest już w grupie"));
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
        return ResponseEntity.status(HttpStatus.OK).body(new Error(""));
    }

    @PostMapping("/usunstudentagrupa")
    public ResponseEntity<?> removeStudentFromGroup(@RequestParam int studentId, HttpServletRequest request) {
        logger.info("Usuwanie studenta o id: {} z grupy z adresu: {}", studentId, request.getRemoteAddr());
        if (studentRepository.findById(studentId).isEmpty()) {
            logger.error("Nie ma takiego studenta o id: {} z adresu: {}", studentId, request.getRemoteAddr());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error("Nie ma takiego studenta"));
        }
        Student student = studentRepository.findById(studentId).get();
        student.setGrupaId(null);
        studentRepository.save(student);
        obecnoscRepository.deleteAllByStudentId(studentId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Error(""));
    }

    @PostMapping("/dodajtermin")
    public ResponseEntity<?> addTerm(@RequestParam int grupaId, @RequestParam String nazwa, @RequestParam String data, @RequestParam int prowadzacyId, HttpServletRequest request) {
        logger.info("Dodawanie terminu: {} {} {} {} z adresu: {}", grupaId, nazwa, data, prowadzacyId, request.getRemoteAddr());
        Termin termin = new Termin();
        termin.setGrupaId(grupaId);
        if(grupaRepository.findById(grupaId).isEmpty()){
            logger.error("Nie ma takiej grupy o id: {} z adresu: {}", grupaId, request.getRemoteAddr());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error("Nie ma takiej grupy"));
        }
        termin.setNazwa(nazwa);
        termin.setData(data);
        termin.setProwadzacyId(prowadzacyId);
        if(prowadzacyRepository.findById(prowadzacyId).isEmpty()){
            logger.error("Nie ma takiego prowadzacego o id: {} z adresu: {}", prowadzacyId, request.getRemoteAddr());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error("Nie ma takiego prowadzacego"));
        }
        terminRepository.save(termin);
        for(Student s: studentRepository.findByGrupaId(grupaId)){
            Obecnosc obecnosc = new Obecnosc();
            obecnosc.setStudentId(s.getIndeks());
            obecnosc.setTerminId(termin.getId());
            obecnosc.setAttendance(0);
            obecnoscRepository.save(obecnosc);
        }
        return ResponseEntity.status(HttpStatus.OK).body(new Error(""));
    }

    @PostMapping("/usuntermin")
    public ResponseEntity<?> removeTerm(@RequestParam int terminId, HttpServletRequest request) {
        if(terminRepository.findById(terminId).isEmpty()){
            logger.error("Nie ma takiego terminu o id: {} z adresu: {}", terminId, request.getRemoteAddr());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error("Nie ma takiego terminu"));
        }
        logger.info("Usuwanie terminu o id: {} z adresu: {}", terminId, request.getRemoteAddr());
        terminRepository.deleteById(terminId);
        obecnoscRepository.deleteAllByTerminId(terminId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Error(""));
    }

    @GetMapping("/termingrupa")
    public ResponseEntity<?> getTermsFromGroup(@RequestParam int grupaId, HttpServletRequest request) {
        if(grupaRepository.findById(grupaId).isEmpty()){
            logger.error("Nie ma takiej grupy o id: {} z adresu: {}", grupaId, request.getRemoteAddr());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error("Nie ma takiej grupy"));
        }
        logger.info("Pobieranie terminów z grupy o id: {} z adresu: {}", grupaId, request.getRemoteAddr());
        return ResponseEntity.status(HttpStatus.OK).body(terminRepository.findByGrupaId(grupaId));
    }

    @GetMapping("/sprawdzobecnosc")
    public ResponseEntity<?> checkAttendance(@RequestParam int terminId, HttpServletRequest request) {
        if(terminRepository.findById(terminId).isEmpty()){
            logger.error("Nie ma takiego terminu o id: {} z adresu: {}", terminId, request.getRemoteAddr());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error("Nie ma takiego terminu"));
        }
        logger.info("Sprawdzanie obecności na terminie o id: {} z adresu: {}", terminId, request.getRemoteAddr());
        return ResponseEntity.status(HttpStatus.OK).body(obecnoscRepository.findByTerminId(terminId));
    }

    @GetMapping("/sprawdzobecnoscstudenta")
    public ResponseEntity<?> checkStudentAttendanceAtTerm(@RequestParam int studentId, @RequestParam int terminId, HttpServletRequest request) {
        logger.info("Sprawdzanie obecności studenta o id: {} na terminie o id: {} z adresu: {}", studentId, terminId, request.getRemoteAddr());
        if(studentRepository.findById(studentId).isEmpty()){
            logger.error("Nie ma takiego studenta o id: {} z adresu: {}", studentId, request.getRemoteAddr());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error("Nie ma takiego studenta"));
        }
        if(terminRepository.findById(terminId).isEmpty()){
            logger.error("Nie ma takiego terminu o id: {} z adresu: {}", terminId, request.getRemoteAddr());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error("Nie ma takiego terminu"));
        }
        if(obecnoscRepository.findByStudentIdAndTerminId(studentId, terminId) == null){
            logger.error("Nie ma takiej obecności studenta o id: {} na terminie o id: {} z adresu: {}", studentId, terminId, request.getRemoteAddr());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error("Nie ma takiej obecności"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(obecnoscRepository.findByStudentIdAndTerminId(studentId, terminId));
    }

    @GetMapping("/grupy")
    public ResponseEntity<?> getGroups(HttpServletRequest request) {
        logger.info("Pobieranie grup z adresu: {}", request.getRemoteAddr());
        return ResponseEntity.status(HttpStatus.OK).body(grupaRepository.findAll());
    }

    @PostMapping("/dodajgrupe")
    public ResponseEntity<?> addGroup(@RequestParam String nazwa, HttpServletRequest request) {
        logger.info("Dodawanie grupy: {} z adresu: {}", nazwa, request.getRemoteAddr());
        Grupa grupa = new Grupa();
        grupa.setNazwa(nazwa);
        try{
            grupaRepository.save(grupa);
            return ResponseEntity.status(HttpStatus.OK).body(new Error(""));
        } catch (Exception e) {
            logger.error("Błąd dodawania grupy: {} z adresu: {}", nazwa, request.getRemoteAddr());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error("Błąd dodawania grupy"));
        }
    }

    @PostMapping("/usungrupe")
    public ResponseEntity<?> removeGroup(@RequestParam int grupaId, HttpServletRequest request) {
        logger.info("Usuwanie grupy o id: {} z adresu: {}", grupaId, request.getRemoteAddr());
        if(grupaRepository.findById(grupaId).isEmpty()){
            logger.error("Nie ma takiej grupy o id: {} z adresu: {}", grupaId, request.getRemoteAddr());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error("Nie ma takiej grupy"));
        }
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
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Error(""));
    }

    @GetMapping("/dziennik")
    public ResponseEntity<?> checkAttendanceList(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(new Error("Nie zaimplementowano"));
    }
}
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Kontroler API obsługujący różne operacje związane z zarządzaniem studentami, grupami, terminami i obecnościami.
 */
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

    /**
     * Pobiera listę wszystkich studentów.
     *
     * @param request Obiekt HttpServletRequest zawierający informacje o żądaniu HTTP.
     * @return ResponseEntity z listą studentów.
     */
    @GetMapping("/studenci")
    public ResponseEntity<?> getStudents(HttpServletRequest request) {
        logger.info("Pobieranie studentów z adresu: {}", request.getRemoteAddr());
        return ResponseEntity.status(HttpStatus.OK).body(studentRepository.findAll());
    }

    /**
     * Pobiera listę studentów z określonej grupy.
     *
     * @param grupaId Identyfikator grupy.
     * @param request Obiekt HttpServletRequest zawierający informacje o żądaniu HTTP.
     * @return ResponseEntity z listą studentów z grupy lub błędem, jeśli grupa nie istnieje.
     */
    @GetMapping("/studencigrupa")
    public ResponseEntity<?> getStudentsFromGroup(@RequestParam int grupaId, HttpServletRequest request) {
        if (!grupaRepository.existsById(grupaId)) {
            logger.error("Nie ma takiej grupy o id: {} z adresu: {}", grupaId, request.getRemoteAddr());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error("Nie ma takiej grupy"));
        }
        logger.info("Pobieranie studentów z grupy o id: {} z adresu: {}", grupaId, request.getRemoteAddr());
        return ResponseEntity.status(HttpStatus.OK).body(studentRepository.findByGrupaId(grupaId));
    }

    /**
     * Dodaje nowego studenta.
     *
     * @param indeks   Indeks studenta.
     * @param imie     Imię studenta.
     * @param nazwisko Nazwisko studenta.
     * @param request  Obiekt HttpServletRequest zawierający informacje o żądaniu HTTP.
     * @return ResponseEntity z informacją o sukcesie lub błędem, jeśli student już istnieje.
     */
    @PostMapping("/dodajstudenta")
    public ResponseEntity<?> addStudent(@RequestParam int indeks, @RequestParam String imie, @RequestParam String nazwisko, HttpServletRequest request) {
        if (studentRepository.existsStudentByIndeks(indeks)) {
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

    /**
     * Loguje prowadzącego.
     *
     * @param login    Login prowadzącego.
     * @param password Hasło prowadzącego.
     * @param request  Obiekt HttpServletRequest zawierający informacje o żądaniu HTTP.
     * @return ResponseEntity z informacją o sukcesie lub błędem, jeśli logowanie się nie powiodło.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam int login, @RequestParam String password, HttpServletRequest request) {
        logger.info("Logowanie prowadzacego: {} z adresu: {}", login, request.getRemoteAddr());
        try {
            Prowadzacy prowadzacy = prowadzacyRepository.findByIdAndHaslo(login, password);
            if (prowadzacy == null) {
                logger.error("Błąd logowania prowadzacego: {} z adresu: {}", login, request.getRemoteAddr());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Error("Błąd logowania"));
            }
            return ResponseEntity.status(HttpStatus.OK).body(prowadzacy);
        } catch (Exception e) {
            logger.error("Błąd logowania prowadzacego: {} z adresu: {}", login, request.getRemoteAddr());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Error("Błąd logowania"));
        }
    }

    /**
     * Usuwa studenta.
     *
     * @param id      Identyfikator studenta.
     * @param request Obiekt HttpServletRequest zawierający informacje o żądaniu HTTP.
     * @return ResponseEntity z informacją o sukcesie lub błędem, jeśli student nie istnieje.
     */
    @Transactional
    @PostMapping("/usunstudenta")
    public ResponseEntity<?> removeStudent(@RequestParam int id, HttpServletRequest request) {
        if (!studentRepository.existsById(id)) {
            logger.error("Nie ma takiego studenta o id: {} z adresu: {}", id, request.getRemoteAddr());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error("Nie ma takiego studenta"));
        }
        logger.info("Usuwanie studenta o id: {} z adresu: {}", id, request.getRemoteAddr());
        studentRepository.deleteById(id);
        obecnoscRepository.deleteAllByStudentId(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Error(""));
    }

    /**
     * Dodaje studenta do grupy.
     *
     * @param studentId Identyfikator studenta.
     * @param groupId   Identyfikator grupy.
     * @param request   Obiekt HttpServletRequest zawierający informacje o żądaniu HTTP.
     * @return ResponseEntity z informacją o sukcesie lub błędem, jeśli student lub grupa nie istnieje.
     */
    @PostMapping("/dodajstudentagrupa")
    public ResponseEntity<?> addStudentToGroup(@RequestParam int studentId, @RequestParam int groupId, HttpServletRequest request) {
        logger.info("Dodawanie studenta o id: {} do grupy o id: {} z adresu: {}", studentId, groupId, request.getRemoteAddr());
        if (!studentRepository.existsById(studentId)) {
            logger.error("Nie ma takiego studenta o id: {} z adresu: {}", studentId, request.getRemoteAddr());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error("Nie ma takiego studenta"));
        }
        if (studentRepository.findById(studentId).get().getGrupaId() != null) {
            logger.error("Student o id: {} jest już w grupie o id: {} z adresu: {}", studentId, studentRepository.findById(studentId).get().getGrupaId(), request.getRemoteAddr());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new Error("Student jest już w grupie"));
        }
        Student student = studentRepository.findById(studentId).get();
        student.setGrupaId(groupId);
        studentRepository.save(student);
        for (Termin t : terminRepository.findByGrupaId(groupId)) {
            Obecnosc obecnosc = new Obecnosc();
            obecnosc.setStudentId(studentId);
            obecnosc.setTerminId(t.getId());
            obecnosc.setAttendance(0);
            obecnoscRepository.save(obecnosc);
        }
        return ResponseEntity.status(HttpStatus.OK).body(new Error(""));
    }

    /**
     * Usuwa studenta z grupy.
     *
     * @param studentId Identyfikator studenta.
     * @param request   Obiekt HttpServletRequest zawierający informacje o żądaniu HTTP.
     * @return ResponseEntity z informacją o sukcesie lub błędem, jeśli student nie istnieje.
     */
    @Transactional
    @PostMapping("/usunstudentagrupa")
    public ResponseEntity<?> removeStudentFromGroup(@RequestParam int studentId, HttpServletRequest request) {
        logger.info("Usuwanie studenta o id: {} z grupy z adresu: {}", studentId, request.getRemoteAddr());
        if (!studentRepository.existsById(studentId)) {
            logger.error("Nie ma takiego studenta o id: {} z adresu: {}", studentId, request.getRemoteAddr());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error("Nie ma takiego studenta"));
        }
        Student student = studentRepository.findById(studentId).get();
        student.setGrupaId(null);
        studentRepository.save(student);
        obecnoscRepository.deleteAllByStudentId(studentId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Error(""));
    }

    /**
     * Dodaje nowy termin do grupy.
     *
     * @param grupaId      Identyfikator grupy.
     * @param nazwa        Nazwa terminu.
     * @param data         Data terminu.
     * @param prowadzacyId Identyfikator prowadzącego.
     * @param request      Obiekt HttpServletRequest zawierający informacje o żądaniu HTTP.
     * @return ResponseEntity z informacją o sukcesie lub błędem, jeśli grupa lub prowadzący nie istnieje.
     */
    @PostMapping("/dodajtermin")
    public ResponseEntity<?> addTerm(@RequestParam int grupaId, @RequestParam String nazwa, @RequestParam String data, @RequestParam int prowadzacyId, HttpServletRequest request) {
        logger.info("Dodawanie terminu do grupy o id: {} z prowadzącym {} z adresu: {}", grupaId, prowadzacyId, request.getRemoteAddr());
        if (!grupaRepository.existsById(grupaId)) {
            logger.error("Nie ma takiej grupy o id: {} z adresu: {}", grupaId, request.getRemoteAddr());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error("Nie ma takiej grupy"));
        }
        if (!terminRepository.existsByDataAndGrupaId(data, grupaId)) {
            logger.error("Termin o nazwie: {} już istnieje w grupie o id: {} z adresu: {}", nazwa, grupaId, request.getRemoteAddr());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new Error("Termin o podanej dacie już istnieje"));
        }
        if (!prowadzacyRepository.existsById(prowadzacyId)) {
            logger.error("Nie ma takiego prowadzacego o id: {} z adresu: {}", prowadzacyId, request.getRemoteAddr());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error("Nie ma takiego prowadzacego"));
        }
        Termin termin = new Termin();
        termin.setNazwa(nazwa);
        termin.setGrupaId(grupaId);
        termin.setData(data);
        termin.setProwadzacyId(prowadzacyId);
        terminRepository.save(termin);
        for (Student s : studentRepository.findByGrupaId(grupaId)) {
            Obecnosc obecnosc = new Obecnosc();
            obecnosc.setStudentId(s.getIndeks());
            obecnosc.setTerminId(termin.getId());
            obecnosc.setAttendance(0);
            obecnoscRepository.save(obecnosc);
        }
        return ResponseEntity.status(HttpStatus.OK).body(new Error(""));
    }

    /**
     * Usuwa termin.
     *
     * @param terminId Identyfikator terminu.
     * @param request  Obiekt HttpServletRequest zawierający informacje o żądaniu HTTP.
     * @return ResponseEntity z informacją o sukcesie lub błędem, jeśli termin nie istnieje.
     */
    @Transactional
    @PostMapping("/usuntermin")
    public ResponseEntity<?> removeTerm(@RequestParam int terminId, HttpServletRequest request) {
        if (!terminRepository.existsById(terminId)) {
            logger.error("Nie ma takiego terminu o id: {} z adresu: {}", terminId, request.getRemoteAddr());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error("Nie ma takiego terminu"));
        }
        logger.info("Usuwanie terminu o id: {} z adresu: {}", terminId, request.getRemoteAddr());
        terminRepository.deleteById(terminId);
        obecnoscRepository.deleteAllByTerminId(terminId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Error(""));
    }

    /**
     * Pobiera listę terminów z określonej grupy.
     *
     * @param grupaId Identyfikator grupy.
     * @param request Obiekt HttpServletRequest zawierający informacje o żądaniu HTTP.
     * @return ResponseEntity z listą terminów z grupy lub błędem, jeśli grupa nie istnieje.
     */
    @GetMapping("/termingrupa")
    public ResponseEntity<?> getTermsFromGroup(@RequestParam int grupaId, HttpServletRequest request) {
        if (!grupaRepository.existsById(grupaId)) {
            logger.error("Nie ma takiej grupy o id: {} z adresu: {}", grupaId, request.getRemoteAddr());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error("Nie ma takiej grupy"));
        }
        logger.info("Pobieranie terminów z grupy o id: {} z adresu: {}", grupaId, request.getRemoteAddr());
        return ResponseEntity.status(HttpStatus.OK).body(terminRepository.findByGrupaId(grupaId));
    }

    /**
     * Sprawdza obecność na określonym terminie.
     *
     * @param terminId Identyfikator terminu.
     * @param request  Obiekt HttpServletRequest zawierający informacje o żądaniu HTTP.
     * @return ResponseEntity z listą obecności na terminie lub błędem, jeśli termin nie istnieje.
     */
    @GetMapping("/sprawdzobecnosc")
    public ResponseEntity<?> checkAttendance(@RequestParam int terminId, HttpServletRequest request) {
        if (!terminRepository.existsById(terminId)) {
            logger.error("Nie ma takiego terminu o id: {} z adresu: {}", terminId, request.getRemoteAddr());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error("Nie ma takiego terminu"));
        }
        logger.info("Sprawdzanie obecności na terminie o id: {} z adresu: {}", terminId, request.getRemoteAddr());
        return ResponseEntity.status(HttpStatus.OK).body(obecnoscRepository.findByTerminId(terminId));
    }

    /**
     * Sprawdza obecność studenta na określonym terminie.
     *
     * @param studentId Identyfikator studenta.
     * @param terminId  Identyfikator terminu.
     * @param request   Obiekt HttpServletRequest zawierający informacje o żądaniu HTTP.
     * @return ResponseEntity z informacją o obecności studenta na terminie lub błędem, jeśli student lub termin nie istnieje.
     */
    @GetMapping("/sprawdzobecnoscstudenta")
    public ResponseEntity<?> checkStudentAttendanceAtTerm(@RequestParam int studentId, @RequestParam int terminId, HttpServletRequest request) {
        logger.info("Sprawdzanie obecności studenta o id: {} na terminie o id: {} z adresu: {}", studentId, terminId, request.getRemoteAddr());
        if (!studentRepository.existsById(studentId)) {
            logger.error("Nie ma takiego studenta o id: {} z adresu: {}", studentId, request.getRemoteAddr());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error("Nie ma takiego studenta"));
        }
        if (!terminRepository.existsById(terminId)) {
            logger.error("Nie ma takiego terminu o id: {} z adresu: {}", terminId, request.getRemoteAddr());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error("Nie ma takiego terminu"));
        }
        if (obecnoscRepository.findByStudentIdAndTerminId(studentId, terminId) == null) {
            logger.error("Nie ma takiej obecności studenta o id: {} na terminie o id: {} z adresu: {}", studentId, terminId, request.getRemoteAddr());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error("Nie ma takiej obecności"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(obecnoscRepository.findByStudentIdAndTerminId(studentId, terminId));
    }

    /**
     * Zmienia obecność studenta na określonym terminie.
     *
     * @param studentId  Identyfikator studenta.
     * @param terminId   Identyfikator terminu.
     * @param attendance Nowa wartość obecności.
     * @param request    Obiekt HttpServletRequest zawierający informacje o żądaniu HTTP.
     * @return ResponseEntity z informacją o sukcesie lub błędem, jeśli student, termin lub obecność nie istnieje.
     */
    @PostMapping("/zmienobecnosc")
    public ResponseEntity<?> changeAttendance(@RequestParam int studentId, @RequestParam int terminId, @RequestParam int attendance, HttpServletRequest request) {
        logger.info("Zmiana obecności studenta o id: {} na terminie o id: {} na: {} z adresu: {}", studentId, terminId, attendance, request.getRemoteAddr());
        if (!studentRepository.existsById(studentId)) {
            logger.error("Nie ma takiego studenta o id: {} z adresu: {}", studentId, request.getRemoteAddr());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error("Nie ma takiego studenta"));
        }
        if (!terminRepository.existsById(terminId)) {
            logger.error("Nie ma takiego terminu o id: {} z adresu: {}", terminId, request.getRemoteAddr());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error("Nie ma takiego terminu"));
        }
        if (obecnoscRepository.findByStudentIdAndTerminId(studentId, terminId) == null) {
            logger.error("Nie ma takiej obecności studenta o id: {} na terminie o id: {} z adresu: {}", studentId, terminId, request.getRemoteAddr());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error("Nie ma takiej obecności"));
        }
        Obecnosc obecnosc = obecnoscRepository.findByStudentIdAndTerminId(studentId, terminId);
        obecnosc.setAttendance(attendance);
        obecnoscRepository.save(obecnosc);
        return ResponseEntity.status(HttpStatus.OK).body(new Error(""));
    }

    /**
     * Pobiera listę wszystkich grup.
     *
     * @param request Obiekt HttpServletRequest zawierający informacje o żądaniu HTTP.
     * @return ResponseEntity z listą grup.
     */
    @GetMapping("/grupy")
    public ResponseEntity<?> getGroups(HttpServletRequest request) {
        logger.info("Pobieranie grup z adresu: {}", request.getRemoteAddr());
        return ResponseEntity.status(HttpStatus.OK).body(grupaRepository.findAll());
    }

    /**
     * Dodaje nową grupę.
     *
     * @param nazwa   Nazwa grupy.
     * @param request Obiekt HttpServletRequest zawierający informacje o żądaniu HTTP.
     * @return ResponseEntity z informacją o sukcesie lub błędem, jeśli dodanie grupy się nie powiodło.
     */
    @PostMapping("/dodajgrupe")
    public ResponseEntity<?> addGroup(@RequestParam String nazwa, HttpServletRequest request) {
        logger.info("Dodawanie grupy: {} z adresu: {}", nazwa, request.getRemoteAddr());
        Grupa grupa = new Grupa();
        grupa.setNazwa(nazwa);
        try {
            grupaRepository.save(grupa);
            return ResponseEntity.status(HttpStatus.OK).body(new java.lang.Error(""));
        } catch (Exception e) {
            logger.error("Błąd dodawania grupy: {} z adresu: {}", nazwa, request.getRemoteAddr());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new java.lang.Error("Błąd dodawania grupy"));
        }
    }

    /**
     * Usuwa grupę.
     *
     * @param grupaId Identyfikator grupy.
     * @param request Obiekt HttpServletRequest zawierający informacje o żądaniu HTTP.
     * @return ResponseEntity z informacją o sukcesie lub błędem, jeśli grupa nie istnieje.
     */
    @Transactional
    @PostMapping("/usungrupe")
    public ResponseEntity<?> removeGroup(@RequestParam int grupaId, HttpServletRequest request) {
        logger.info("Usuwanie grupy o id: {} z adresu: {}", grupaId, request.getRemoteAddr());
        if (!grupaRepository.existsById(grupaId)) {
            logger.error("Nie ma takiej grupy o id: {} z adresu: {}", grupaId, request.getRemoteAddr());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new java.lang.Error("Nie ma takiej grupy"));
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
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new java.lang.Error(""));
    }
}
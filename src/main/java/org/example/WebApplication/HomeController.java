package org.example.WebApplication;

import org.example.Database.Objects.Obecnosc;
import org.example.Database.Objects.Termin;
import org.example.Database.Repositories.ObecnoscRepository;
import org.example.Database.Repositories.TerminRepository;
import org.example.WebApplication.Objects.ObecnoscWeb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Kontroler obsługujący żądania związane z główną stroną aplikacji internetowej.
 */

@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private ObecnoscRepository obecnoscRepository;
    @Autowired
    private TerminRepository terminRepository;

    /**
     * Obsługuje żądanie GET dla strony głównej.
     *
     * @param index  Indeks studenta jako atrybut modelu.
     * @param model  Obiekt Model do przekazywania danych do widoku.
     * @return Nazwa widoku "home".
     */
    @GetMapping("/home")
    public String home(@ModelAttribute("index") String index, Model model) {
        logger.info("Wyświetlanie strony głównej dla studenta o indeksie: {}", index);
        List<ObecnoscWeb> obecnosci = new ArrayList<>();
        try {
            List<Obecnosc> obecnosciDB = obecnoscRepository.findByStudentId(Integer.parseInt(index));

            for (Obecnosc obecnosc : obecnosciDB) {
                if (terminRepository.findById(obecnosc.getTerminId()).isEmpty()){
                    continue;
                }
                Termin termin = terminRepository.findById(obecnosc.getTerminId()).get();
                ObecnoscWeb o = new ObecnoscWeb();
                o.setNazwa(termin.getNazwa());
                o.setData(String.valueOf(termin.getData()));
                o.setAttendance(obecnosc.getAttendance());
                obecnosci.add(o);
            }
        } catch (IllegalArgumentException | NoSuchElementException e) {
            e.printStackTrace();
        }
        model.addAttribute("obecnosci", obecnosci);
        model.addAttribute("lista", new ArrayList<String>());
        return "home";
    }
}
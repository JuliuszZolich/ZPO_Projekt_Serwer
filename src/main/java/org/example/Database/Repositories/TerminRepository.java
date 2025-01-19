package org.example.Database.Repositories;

import org.example.Database.Objects.Termin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repozytorium dla obiektów Termin.
 */
@Repository
public interface TerminRepository extends JpaRepository<Termin, Integer> {

    /**
     * Znajduje wszystkie obiekty Termin dla danego identyfikatora grupy.
     *
     * @param grupaId Identyfikator grupy.
     * @return Lista obiektów Termin.
     */
    List<Termin> findByGrupaId(int grupaId);

    /**
     * Sprawdza, czy istnieje termin o podanej dacie i identyfikatorze grupy.
     *
     * @param data    Data terminu.
     * @param grupaId Identyfikator grupy.
     * @return true, jeśli termin o podanej dacie i identyfikatorze grupy istnieje, w przeciwnym razie false.
     */
    boolean existsByDataAndGrupaId(String data, int grupaId);
}
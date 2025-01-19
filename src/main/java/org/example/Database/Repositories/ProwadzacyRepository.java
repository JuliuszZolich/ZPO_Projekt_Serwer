package org.example.Database.Repositories;

import org.example.Database.Objects.Prowadzacy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repozytorium dla obiektów Prowadzacy.
 */
@Repository
public interface ProwadzacyRepository extends JpaRepository<Prowadzacy, Integer> {

    /**
     * Znajduje obiekt Prowadzacy na podstawie identyfikatora i hasła.
     *
     * @param id Identyfikator prowadzącego.
     * @param haslo Hasło prowadzącego.
     * @return Obiekt Prowadzacy.
     */
    Prowadzacy findByIdAndHaslo(int id, String haslo);
}
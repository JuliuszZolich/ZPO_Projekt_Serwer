package org.example.Database.Repositories;

import org.example.Database.Objects.Prowadzacy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProwadzacyRepository extends JpaRepository<Prowadzacy, Integer> {
    Prowadzacy findByIdAndHaslo(int id, String haslo);
}


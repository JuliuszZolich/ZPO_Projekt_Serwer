package org.example.Database.Repositories;

import org.example.Database.Objects.Grupa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repozytorium dla obiektów Grupa.
 */

@Repository
public interface GrupaRepository extends JpaRepository<Grupa, Integer> {
}


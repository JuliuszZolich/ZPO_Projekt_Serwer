package org.example.Database.Repositories;

import org.example.Database.Objects.Termin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TerminRepository extends JpaRepository<Termin, Integer> {
    List<Termin> findByGrupaId(int grupaId);

    Termin findByGrupaIdAndData(int grupaId, String data);

    boolean existsByDataAndGrupaId(String data, int grupaId);
}


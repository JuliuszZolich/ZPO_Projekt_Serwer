package org.example.Database.Repositories;

import org.example.Database.Objects.Obecnosc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ObecnoscRepository extends JpaRepository<Obecnosc, Integer> {
    List<Obecnosc> findByStudentId(int studentId);
    Obecnosc findByStudentIdAndTerminId(int studentId, int terminId);
    List<Obecnosc> findByTerminId(int terminId);
}


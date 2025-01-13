package org.example.Database.Repositories;

import org.example.Database.Objects.Obecnosc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ObecnoscRepository extends JpaRepository<Obecnosc, Integer> {
    List<Obecnosc> findBystudentId(int studentId);
}


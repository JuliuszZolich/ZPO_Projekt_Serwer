package org.example.Database.Repositories;

import org.example.Database.Objects.Obecnosc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repozytorium dla obiektów Obecnosc.
 */

@Repository
public interface ObecnoscRepository extends JpaRepository<Obecnosc, Integer> {

    /**
     * Znajduje wszystkie obiekty Obecnosc dla danego identyfikatora studenta.
     *
     * @param studentId Identyfikator studenta.
     * @return Lista obiektów Obecnosc.
     */
    List<Obecnosc> findByStudentId(int studentId);

    /**
     * Znajduje wszystkie obiekty Obecnosc dla danego identyfikatora terminu.
     *
     * @param terminId Identyfikator terminu.
     * @return Lista obiektów Obecnosc.
     */
    List<Obecnosc> findByTerminId(int terminId);

    /**
     * Znajduje obiekt Obecnosc dla danego identyfikatora studenta i terminu.
     *
     * @param studentId Identyfikator studenta.
     * @param terminId Identyfikator terminu.
     * @return Obiekt Obecnosc.
     */
    Obecnosc findByStudentIdAndTerminId(int studentId, int terminId);

    /**
     * Usuwa wszystkie obiekty Obecnosc dla danego identyfikatora studenta.
     *
     * @param studentId Identyfikator studenta.
     */
    void deleteAllByStudentId(int studentId);

    /**
     * Usuwa wszystkie obiekty Obecnosc dla danego identyfikatora terminu.
     *
     * @param terminId Identyfikator terminu.
     */
    void deleteAllByTerminId(int terminId);
}
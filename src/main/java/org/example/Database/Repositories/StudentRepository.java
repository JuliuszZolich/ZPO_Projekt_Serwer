package org.example.Database.Repositories;

import org.example.Database.Objects.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repozytorium dla obiektów Student.
 */

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

    /**
     * Znajduje wszystkich studentów dla danego identyfikatora grupy.
     *
     * @param grupaId Identyfikator grupy.
     * @return Lista obiektów Student.
     */
    List<Student> findByGrupaId(Integer grupaId);

    /**
     * Sprawdza, czy istnieje student o podanym indeksie.
     *
     * @param indeks Indeks studenta.
     * @return true, jeśli student o podanym indeksie istnieje, w przeciwnym razie false.
     */
    boolean existsStudentByIndeks(int indeks);
}
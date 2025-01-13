package org.example.Database.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter @Setter
public class Student implements Serializable {
        @Id
        private int indeks;
        @Column(nullable = false)
        private String imie;
        @Column(nullable = false)
        private String nazwisko;
        @Column()
        private Integer grupaId;

        public Student() {
        }
}

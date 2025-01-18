package org.example.Database.Objects;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter @Setter
public class Obecnosc implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private int attendance;
    @Column(nullable = false)
    private int studentId;
    @Column(nullable = false)
    private int terminId;
}

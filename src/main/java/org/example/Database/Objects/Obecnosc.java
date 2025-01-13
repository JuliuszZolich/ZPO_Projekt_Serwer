package org.example.Database.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter @Setter
public class Obecnosc implements Serializable {
    @Id
    private int id;
    @Column(nullable = false)
    private int attendance;
    @Column(nullable = false)
    private int studentId;
    @Column(nullable = false)
    private int terminId;
}

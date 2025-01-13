package org.example.Database.Objects;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Date;

@Entity
@Getter @Setter
public class Termin implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private int grupaId;
    @Column()
    private String nazwa;
    @Column(nullable = false)
    private String data;
    @Column(nullable = false)
    private int prowadzacyId;
}

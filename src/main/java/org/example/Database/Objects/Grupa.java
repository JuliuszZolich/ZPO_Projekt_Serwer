package org.example.Database.Objects;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

@Entity
@Setter @Getter
public class Grupa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;
    @Column(nullable = false)
    private String nazwa;
}

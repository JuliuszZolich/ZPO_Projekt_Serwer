package org.example.WebApplication.Objects;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter @Setter public class Termin {
    private int id;
    private int grupa_id;
    private String nazwa;
    private Date data;
    private int prowadzacy_id;
}

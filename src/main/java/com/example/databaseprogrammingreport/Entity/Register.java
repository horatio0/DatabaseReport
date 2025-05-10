package com.example.databaseprogrammingreport.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@Table
@NoArgsConstructor
@AllArgsConstructor
public class Register {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int registerId;

    @NotNull
    private String counselorId;
    @NotNull
    private String clientId;
    @NotNull
    private int year;
    @NotNull
    private int month;
    @NotNull
    private int day;
    @NotNull
    private int hour;
    @NotNull
    private int minute;
    private String request;
}

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
public class RegisterRequest {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int registerId;

    @NotNull
    private String counselorId;
    private String clientId;
    private String clientName;
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

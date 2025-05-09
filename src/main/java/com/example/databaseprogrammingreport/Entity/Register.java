package com.example.databaseprogrammingreport.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@Table
@NoArgsConstructor
@AllArgsConstructor
public class Register {
    @Id
    private int registerId;

    private String clientId;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private String request;
}

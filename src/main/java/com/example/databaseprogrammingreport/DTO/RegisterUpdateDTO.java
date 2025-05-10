package com.example.databaseprogrammingreport.DTO;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RegisterUpdateDTO {
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private String request;
}

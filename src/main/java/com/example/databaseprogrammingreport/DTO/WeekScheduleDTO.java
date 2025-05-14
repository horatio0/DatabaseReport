package com.example.databaseprogrammingreport.DTO;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WeekScheduleDTO {
    private String clientName;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
}

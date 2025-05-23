package com.example.databaseprogrammingreport.Entity;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DayScheduleDTO {
    int hour;
    int minute;
    String clientName;
}

package com.example.databaseprogrammingreport.DTO;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyCounselDTO {
    private long counselId;
    private String counselorName;
    private String title;
    private String date;
}

package com.example.databaseprogrammingreport.DTO;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyRegisterListDTO {
    private int registerId;
    private String counselorName;
    private String date;
    private Boolean isAccepted;
    private Boolean haveCounselReport;

}

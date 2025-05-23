package com.example.databaseprogrammingreport.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Introduction {
    @Id
    private String counselorId;
    private String counselorName;
    private String expertise;
    private String school;
    private String career;
    private String introduction;
}

package com.example.databaseprogrammingreport.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table
@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Counsel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long counselId;
    private int registerId;

    private String counselorId;
    @NotNull
    private String clientId;

    private String topic;       //주제
    private String date;        //날짜
    private String content;     //내용
    private String type;        //상담 방식
    private String result;      //상담 결과 요약
    private String clientStatus;    //상담자 상태
    private boolean confidential;   //기밀 상태
}

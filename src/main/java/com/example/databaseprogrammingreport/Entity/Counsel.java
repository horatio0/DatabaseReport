package com.example.databaseprogrammingreport.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Counsel {
    @Id
    private String counselId;

    private String counselorId;
    private String clientId;
    private String createdBy;

    private String topic;       //주제
    private String date;        //날짜
    private String content;     //내용
    private String type;        //상담 방식
    private String result;      //상담 결과 요약
    private String clientStatus;    //상담자 상태
    private boolean confidential;   //기밀 상태
}

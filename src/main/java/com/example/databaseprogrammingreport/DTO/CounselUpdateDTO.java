package com.example.databaseprogrammingreport.DTO;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CounselUpdateDTO {
    private long counselId;

    private String topic;       //주제
    private String date;        //날짜
    private String content;     //내용
    private String type;        //상담 방식
    private String result;      //상담 결과 요약
    private String clientStatus;    //상담자 상태
    private boolean confidential;   //기밀 상태
}

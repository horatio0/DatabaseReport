package com.example.databaseprogrammingreport.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long messageId;

    private String senderId;
    private String senderName;
    private String receiverId;
    private String title;
    private String content;
    private Boolean isRead;

    //상담 취소 시 쪽지 발송하는 기능 구현할 것

}

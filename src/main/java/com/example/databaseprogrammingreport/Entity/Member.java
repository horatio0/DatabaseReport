package com.example.databaseprogrammingreport.Entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Table
@Entity
@Setter
@Getter
public class Member {
    @Id
    private String memberId;
    private String role;
    private String password;
    private String name;
    private String email;
    private String phoneNumber;
    private String nickname;
}

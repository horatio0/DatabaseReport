package com.example.databaseprogrammingreport.Entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    private String name;
    @Email
    private String email;
    private String phoneNumber;
    private String nickname;
}

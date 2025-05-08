package com.example.databaseprogrammingreport.DTO;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MemberInfoDTO {
    String nickname;
    String memberId;
    String role;
    String email;
    String phoneNumber;
    String name;
}

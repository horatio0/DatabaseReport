package com.example.databaseprogrammingreport.Repository;

import com.example.databaseprogrammingreport.Entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String> {
    Boolean existsByNickname(String nickname);
}

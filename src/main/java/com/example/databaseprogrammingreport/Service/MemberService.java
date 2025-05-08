package com.example.databaseprogrammingreport.Service;

import com.example.databaseprogrammingreport.DTO.MemberInfoDTO;
import com.example.databaseprogrammingreport.Entity.Member;
import com.example.databaseprogrammingreport.Repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder bCryptPasswordEncoder;

    //Create Member
    public ResponseEntity<?> join(Member member){
        try {
            member.setPassword(bCryptPasswordEncoder.encode(member.getPassword()));
            memberRepository.save(member);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    //Check Member Exist
    public Boolean existById(String memberId){
        return memberRepository.existsById(memberId);
    }

    //Check Nickname Exist
    public Boolean existByNickName(String nickname){
        return memberRepository.existsByNickname(nickname);
    }

    //Read Member
    public MemberInfoDTO readMember(String memberId){
        Member member = memberRepository.findById(memberId).get();
        MemberInfoDTO memberInfoDTO = MemberInfoDTO.builder()
                .email(member.getEmail())
                .role(member.getRole())
                .memberId(member.getMemberId())
                .name(member.getName())
                .nickname(member.getNickname())
                .phoneNumber(member.getPhoneNumber())
                .build();
        return memberInfoDTO;
    }

    //Update Member (Not Password)
    public ResponseEntity<?> updateMember(MemberInfoDTO memberInfoDTO, String memberId){
        try {
            Member member = memberRepository.getReferenceById(memberId);
            member.setEmail(memberInfoDTO.getEmail());
            member.setName(memberInfoDTO.getName());
            member.setNickname(memberInfoDTO.getNickname());
            member.setPhoneNumber(memberInfoDTO.getPhoneNumber());
            return ResponseEntity.ok().build();
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    //Update Member Password
    public ResponseEntity<?> updatePassword(String memberId, String password){
        try{
            memberRepository.getReferenceById(memberId).setPassword(bCryptPasswordEncoder.encode(password));
            return ResponseEntity.ok().build();
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    //Delete Member
    public ResponseEntity<?> delete(String memberId){
        try {
            memberRepository.deleteById(memberId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

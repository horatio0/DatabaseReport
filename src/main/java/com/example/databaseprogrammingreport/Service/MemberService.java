package com.example.databaseprogrammingreport.Service;

import com.example.databaseprogrammingreport.DTO.MemberInfoDTO;
import com.example.databaseprogrammingreport.Entity.Member;
import com.example.databaseprogrammingreport.Repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    //Create Member
    public String join(Member member){
        try {
            member.setPassword(bCryptPasswordEncoder.encode(member.getPassword()));
            memberRepository.save(member);
            return "회원가입 성공!";
        }catch (Exception e){
            return "오류 발생!";
        }
    }

    //Check Member Exist
    public Boolean existById(String memberId){
        return memberRepository.existsById(memberId);
    }

    //Check Nickname Exist
    public Boolean existByNickName(String nickname){
        return memberRepository.existByNickname(nickname);
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
    public String updateMember(MemberInfoDTO memberInfoDTO){
        try {
            Member member = memberRepository.findById(memberInfoDTO.getMemberId()).get();
            member.setEmail(memberInfoDTO.getEmail());
            member.setName(memberInfoDTO.getName());
            member.setNickname(memberInfoDTO.getNickname());
            member.setPhoneNumber(memberInfoDTO.getPhoneNumber());
            return "회원 정보 수정 성공!";
        } catch (Exception e){
            return "오류 발생!";
        }
    }

    //Update Member Password
    public String updatePassword(String memberId, String password){
        try{
            memberRepository.getReferenceById(memberId).setPassword(bCryptPasswordEncoder.encode(password));
            return "비밀번호가 변경되었습니다!";
        } catch (Exception e){
            return "오류 발생!";
        }
    }

    //Delete Member
    public String delete(String memberId){
        try {
            memberRepository.deleteById(memberId);
            return "회원 탈퇴 성공!";
        } catch (Exception e) {
            return "오류 발생!";
        }
    }
}

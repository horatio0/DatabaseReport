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
    public void join(Member member) throws Exception{
        member.setPassword(bCryptPasswordEncoder.encode(member.getPassword()));
        memberRepository.save(member);
    }

    //Check Member Exist
    public Boolean existById(String memberId){
        return memberRepository.existsById(memberId);
    }

    //Check Nickname Exist
    public Boolean existByNickName(String nickname){
        return memberRepository.existsByNickname(nickname);
    }

    //Get Name
    public String getName(String memberId){
        return memberRepository.findById(memberId).get().getName();
    }

    //Read Member
    public MemberInfoDTO readMember(String memberId) throws Exception{
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
    public void updateMember(MemberInfoDTO memberInfoDTO, String memberId) throws Exception{
        Member member = memberRepository.getReferenceById(memberId);
        member.setEmail(memberInfoDTO.getEmail());
        member.setName(memberInfoDTO.getName());
        member.setNickname(memberInfoDTO.getNickname());
        member.setPhoneNumber(memberInfoDTO.getPhoneNumber());
    }

    //Update Member Password
    public void updatePassword(String memberId, String password) throws Exception{
        memberRepository.getReferenceById(memberId).setPassword(bCryptPasswordEncoder.encode(password));
    }

    //Delete Member
    public void delete(String memberId) throws Exception{
        memberRepository.deleteById(memberId);
    }
}

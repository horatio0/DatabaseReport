package com.example.databaseprogrammingreport.Service;

import com.example.databaseprogrammingreport.DTO.MemberInfoDTO;
import com.example.databaseprogrammingreport.Entity.Introduction;
import com.example.databaseprogrammingreport.Entity.Member;
import com.example.databaseprogrammingreport.Entity.Register;
import com.example.databaseprogrammingreport.Repository.IntroductionRepository;
import com.example.databaseprogrammingreport.Repository.MemberRepository;
import com.example.databaseprogrammingreport.Repository.RegisterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder bCryptPasswordEncoder;
    private final IntroductionRepository introductionRepository;
    private final RegisterRepository registerRepository;

    //Create Member
    public void join(Member member, boolean isCounselor) throws Exception{
        if (isCounselor) {
            member.setRole("ROLE_COUNSELOR");
        } else {
            member.setRole("ROLE_USER");
        }
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
    public void delete(String memberId, Boolean isCounselor) throws Exception{
        memberRepository.deleteById(memberId);
        if (isCounselor) {
            introductionRepository.deleteById(memberId);
        }
    }

    //상담사 소개 작성
    public void createIntro(Introduction introduction, String id) throws Exception{
        introduction.setCounselorId(id);
        introduction.setCounselorName(getName(id));
        introductionRepository.save(introduction);
    }

    //상담사 소개 수정
    public void updateIntro(Introduction introduction) throws Exception{
        Introduction a = introductionRepository.getReferenceById(introduction.getCounselorId());
        a = Introduction.builder()
                .expertise(introduction.getExpertise())
                .introduction(introduction.getIntroduction())
                .career(introduction.getCareer())
                .school(introduction.getSchool())
                .build();
    }

    //상담사 소개 검색
    public Introduction readIntro(String id) throws Exception{
        try{
            return introductionRepository.findById(id).get();
        } catch (NoSuchElementException e){
            throw new NoSuchElementException("No such counselor");
        }
    }

    //상담사 소개 리스트
    public List<Introduction> readIntroList(){
        return introductionRepository.findAll();
    }

    //전문 분야 목록
    public List<String> readExpertiseList() throws Exception{
        List<Introduction> i = introductionRepository.findAll();

        List<String> a = new ArrayList<>();
        for (Introduction p : i) {
            a.add(p.getExpertise());
        }
        return a;
    }

    //전문분야 검색
    public Map<String, String> readByExpertise(String expertise) throws Exception{
        List<Introduction> i = introductionRepository.findAllByExpertise(expertise);

        Map<String, String> a = new HashMap<>();
        for (Introduction p : i) {
            a.put(getName(p.getCounselorId()), p.getCounselorId());
        }
        return a;
    }

    //시간표 검색
    public List<Integer> readSchedule(String counselorId, LocalDate date) throws Exception{
        List<Register> a = registerRepository.findAllByCounselorIdAndYearAndMonthAndDay(counselorId, date.getYear(), date.getMonthValue(), date.getDayOfMonth());
        Set<Integer> set = new HashSet<>();
        set.add(10);
        set.add(13);
        set.add(15);
        set.add(17);
        for (Register r : a) {
            if (set.contains(r.getHour())) set.remove(r.getHour());
        }

        return set.stream().toList();
    }
}

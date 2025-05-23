package com.example.databaseprogrammingreport.Controller;


import com.example.databaseprogrammingreport.DTO.MemberInfoDTO;
import com.example.databaseprogrammingreport.Entity.Introduction;
import com.example.databaseprogrammingreport.Entity.Member;
import com.example.databaseprogrammingreport.Service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/member/join")
    public ResponseEntity<?> join(@RequestBody @Valid Member member, @RequestParam boolean isCounselor){
        try {
            memberService.join(member, isCounselor);
            return ResponseEntity.ok().build();
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/member/existById")
    public Boolean existById(@RequestParam String memberId){
        return memberService.existById(memberId);
    }

    @GetMapping("/member/existByNickname")
    public Boolean existByNickName(@RequestParam String nickname){
        return memberService.existByNickName(nickname);
    }

    @GetMapping("/member")
    public ResponseEntity<MemberInfoDTO> readMember(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            return ResponseEntity.ok().body(memberService.readMember(userDetails.getUsername()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/member")
    public ResponseEntity<?> updateMember(@RequestBody @Valid MemberInfoDTO memberInfoDTO, @AuthenticationPrincipal UserDetails userDetails){
        try{
            memberService.updateMember(memberInfoDTO, userDetails.getUsername());
            return ResponseEntity.ok().build();
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/member/password")
    public ResponseEntity<?> updatePassword(@RequestBody() String password, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            memberService.updatePassword(userDetails.getUsername(), password);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/member")
    public ResponseEntity<?> deleteMember(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            memberService.delete(userDetails.getUsername(), userDetails.getAuthorities().contains("ROLE_COUNSELOR"));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/counselor/introduction")
    public ResponseEntity<?> createIntroduction(@RequestBody Introduction introduction, @AuthenticationPrincipal UserDetails userDetails){
        try{
            memberService.createIntro(introduction, userDetails.getUsername());
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/introduction")
    public ResponseEntity<List<Introduction>> readIntroductionList(){
        try{
            return ResponseEntity.ok().body(memberService.readIntroList());
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/counselor/introduction")
    public ResponseEntity<Introduction> readIntroduction(@AuthenticationPrincipal UserDetails userDetails){
        try{
            return ResponseEntity.ok().body(memberService.readIntro(userDetails.getUsername()));
        } catch (NoSuchElementException e){
            return ResponseEntity.notFound().build();
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/counselor/introduction")
    public ResponseEntity<?> updateIntroduction(@RequestBody Introduction introduction, @AuthenticationPrincipal UserDetails userDetails){
        try{
            memberService.updateIntro(introduction);
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    //전문분야 목록 검색
    @GetMapping("/client/expertiseList")
    public ResponseEntity<List<String>> readExpertiseList(){
        try{
            return ResponseEntity.ok().body(memberService.readExpertiseList());
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    //전문분야 이용 검색
    @GetMapping("/client/expertise")
    public ResponseEntity<Map<String, String>> readByExpertise(@RequestParam String expertise){
        try{
            return ResponseEntity.ok().body(memberService.readByExpertise(expertise));
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    //시간표 검색
    @GetMapping("/client/timetable")
    public ResponseEntity<List<Integer>> readTimetable(@RequestParam String counselorId, @RequestParam String date){
        LocalDate date1 = LocalDate.parse(date);
        try{
            return ResponseEntity.ok().body(memberService.readSchedule(counselorId, date1));
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
}

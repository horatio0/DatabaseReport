package com.example.databaseprogrammingreport.Controller;


import com.example.databaseprogrammingreport.DTO.MemberInfoDTO;
import com.example.databaseprogrammingreport.Entity.Member;
import com.example.databaseprogrammingreport.Service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/member/join")
    public ResponseEntity<?> join(@RequestBody @Valid Member member){
        try {
            memberService.join(member);
            return ResponseEntity.ok().build();
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/member/existById")
    public Boolean existById(@RequestParam String memberId){
        return memberService.existById(memberId);
    }

    @GetMapping("/member/existByNickName")
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
            memberService.delete(userDetails.getUsername());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

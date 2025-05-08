package com.example.databaseprogrammingreport.Controller;


import com.example.databaseprogrammingreport.DTO.MemberInfoDTO;
import com.example.databaseprogrammingreport.Entity.Member;
import com.example.databaseprogrammingreport.Service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/member/join")
    public String join(@RequestBody() Member member){
        return memberService.join(member);
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
    public MemberInfoDTO readMember(@AuthenticationPrincipal UserDetails userDetails) {
        return memberService.readMember(userDetails.getUsername());
    }

    @PutMapping("/member")
    public String updateMember(@RequestBody() MemberInfoDTO memberInfoDTO, @AuthenticationPrincipal UserDetails userDetails){
        return memberService.updateMember(memberInfoDTO, userDetails.getUsername());
    }

    @PutMapping("/member/password")
    public String updatePassword(@RequestBody() String password, @AuthenticationPrincipal UserDetails userDetails) {
        return memberService.updatePassword(userDetails.getUsername(), password);
    }

    @DeleteMapping("/member")
    public String deleteMember(@AuthenticationPrincipal UserDetails userDetails) {
        return memberService.delete(userDetails.getUsername());
    }
}

package com.example.databaseprogrammingreport.Controller;

import com.example.databaseprogrammingreport.DTO.RegisterUpdateDTO;
import com.example.databaseprogrammingreport.Entity.Register;
import com.example.databaseprogrammingreport.Service.RegisterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class RegisterController {
    private final RegisterService registerService;

    //상담 신청
    @PostMapping("/request")
    public ResponseEntity<?> request(@RequestBody @Valid Register register){
        try {
            registerService.request(register);
            return ResponseEntity.ok().build();
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    //일정 검색
    @GetMapping("/counselor/weekSchedule")
    public ResponseEntity<?> searchWeekSchedule(@AuthenticationPrincipal UserDetails userDetails){
        try{
            return ResponseEntity.ok().body(registerService.readSchedule(userDetails.getUsername()));
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    //상담 수락
    @PostMapping("/counselor/register")
    public ResponseEntity<?> register(@RequestParam int id){
        try {
            registerService.register(id);
            return ResponseEntity.ok().build();
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    //상담 거절
    @DeleteMapping("/counselor/register")
    public ResponseEntity<?> reject(@RequestParam int id, @RequestBody String reason){
        try {
            registerService.reject(id, reason);
            return ResponseEntity.ok().build();
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    //신청자 취소, 수락 전
    @DeleteMapping("/client/notAccept")
    public ResponseEntity<?> cancelRequestBeforeAccept(@RequestParam int id){
        try {
            registerService.cancelRequestBeforeAccept(id);
            return ResponseEntity.ok().build();
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    //신청자 취소, 수락 후
    @DeleteMapping("/client/accept")
    public ResponseEntity<?> cancelRequestAfterAccept(@RequestParam int id, @RequestBody String reason){
        try {
            registerService.cancelRequestAfterAccept(id, reason);
            return ResponseEntity.ok().build();
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    //상담원 취소
    @DeleteMapping("/counselor/cancel")
    public ResponseEntity<?> cancelCounsel(@RequestParam int id, @RequestBody String reason){
        try {
            registerService.cancelCounsel(id, reason);
            return ResponseEntity.ok().build();
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    //수정
    @PutMapping("/register")
    public ResponseEntity<?> updateCounsel(@RequestParam int id, @RequestBody RegisterUpdateDTO registerDTO){
        try {
            registerService.update(id, registerDTO);
            return ResponseEntity.ok().build();
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
}

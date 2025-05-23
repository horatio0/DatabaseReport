package com.example.databaseprogrammingreport.Controller;

import com.example.databaseprogrammingreport.DTO.MyRegisterListDTO;
import com.example.databaseprogrammingreport.DTO.RegisterUpdateDTO;
import com.example.databaseprogrammingreport.Entity.DayScheduleDTO;
import com.example.databaseprogrammingreport.Entity.Register;
import com.example.databaseprogrammingreport.Entity.RegisterRequest;
import com.example.databaseprogrammingreport.Service.RegisterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RegisterController {
    private final RegisterService registerService;

    //상담 신청
    @PostMapping("/request")
    public ResponseEntity<?> request(@RequestBody @Valid RegisterRequest register, @AuthenticationPrincipal UserDetails userDetails){
        try {
            registerService.request(register, userDetails.getUsername());
            return ResponseEntity.ok().build();
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    //상담 신청 목록
    @GetMapping("/myRegisters")
    public ResponseEntity<?> getMyRegisters(@AuthenticationPrincipal UserDetails userDetails){
        try{
            List<MyRegisterListDTO> a = registerService.requestList(userDetails.getUsername());
            return ResponseEntity.ok().body(a);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    //신청받은 상담 목록
    @GetMapping("/counselor/requests")
    public ResponseEntity<?> getRequests(@AuthenticationPrincipal UserDetails userDetails){
        try{
            return ResponseEntity.ok().body(registerService.getRequests(userDetails.getUsername()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    //수락한 상담 목록
    @GetMapping("/counselor/registers")
    public ResponseEntity<?> getRegisters(@AuthenticationPrincipal UserDetails userDetails){
        try{
            return ResponseEntity.ok().body(registerService.getRegisters(userDetails.getUsername()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    //일간 일정 검색
    @GetMapping("/counselor/daySchedule")
    public ResponseEntity<?> searchDaySchedule(@AuthenticationPrincipal UserDetails userDetails){
        try{
            List<DayScheduleDTO> a = registerService.readDaySchedules(userDetails.getUsername());
            return ResponseEntity.ok()
                    .header("today", String.valueOf(a.size()))
                    .body(a);
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    //주간 일정 검색
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
    public ResponseEntity<?> reject(@RequestParam int id, @RequestParam String reason){
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
    public ResponseEntity<?> cancelRequestAfterAccept(@RequestParam int id, @RequestParam String reason){
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

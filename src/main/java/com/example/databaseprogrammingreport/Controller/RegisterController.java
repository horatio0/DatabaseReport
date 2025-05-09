package com.example.databaseprogrammingreport.Controller;

import com.example.databaseprogrammingreport.Entity.Register;
import com.example.databaseprogrammingreport.Service.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class RegisterController {
    private final RegisterService registerService;

    //상담 신청
    @PostMapping("/request")
    public ResponseEntity<?> request(@RequestBody Register register){
        try {
            registerService.request(register);
            return ResponseEntity.ok().build();
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    //상담 수락
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestParam int id){
        try {
            registerService.register(id);
            return ResponseEntity.ok().build();
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    //신청자 취소, 수락 전
    @DeleteMapping("/client/cancel")
    public ResponseEntity<?> cancelRequestBeforeAccept(@RequestParam int id){
        try {
            registerService.cancelRequestBeforeAccept(id);
            return ResponseEntity.ok().build();
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
}

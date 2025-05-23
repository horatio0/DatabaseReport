package com.example.databaseprogrammingreport.Controller;

import com.example.databaseprogrammingreport.DTO.CounselUpdateDTO;
import com.example.databaseprogrammingreport.DTO.MyCounselDTO;
import com.example.databaseprogrammingreport.Entity.Counsel;
import com.example.databaseprogrammingreport.Service.CounselService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CounselController {
    private final CounselService counselService;

    @PostMapping("/counselor/counsel")
    public ResponseEntity<?> counselWrite(@RequestBody @Valid Counsel counsel, @AuthenticationPrincipal UserDetails userDetails){
        try {
            counselService.createCounsel(counsel, userDetails.getUsername());
        } catch (Exception e){
            return ResponseEntity.badRequest().body("오류발생");
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/counselor/myCounsel")
    public ResponseEntity<List<Counsel>> counselRead(@AuthenticationPrincipal UserDetails userDetails){
        try {
            return ResponseEntity.ok().body(counselService.readCounselList(userDetails.getUsername()));
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

//    @PutMapping("/counselor/counsel")
//    public ResponseEntity<?> counselUpdate(@RequestBody CounselUpdateDTO counselUpdateDTO){
//        try {
//            counselService.updateCounsel(counselUpdateDTO);
//        } catch (Exception e){
//            return ResponseEntity.badRequest().body("오류발생");
//        }
//        return ResponseEntity.ok().build();
//    }

    @DeleteMapping("/counselor/counsel")
    public ResponseEntity<?> deleteCounsel(@RequestParam long counselId){
        try {
            counselService.deleteCounsel(counselId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/counsels")
    public ResponseEntity<List<MyCounselDTO>> readCounsels(@AuthenticationPrincipal UserDetails userDetails){
        try{
            return ResponseEntity.ok().body(counselService.readClientHistory(userDetails.getUsername()));
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/counsel")
    public ResponseEntity<Counsel> readCounsel(@RequestParam long counselId){
        try {
            return ResponseEntity.ok().body(counselService.readCounsel(counselId));
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
}

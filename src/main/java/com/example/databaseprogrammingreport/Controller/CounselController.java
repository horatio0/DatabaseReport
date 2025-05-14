package com.example.databaseprogrammingreport.Controller;

import com.example.databaseprogrammingreport.DTO.CounselUpdateDTO;
import com.example.databaseprogrammingreport.Entity.Counsel;
import com.example.databaseprogrammingreport.Service.CounselService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/counselor")
public class CounselController {
    private final CounselService counselService;

    @PostMapping("/counsel")
    public ResponseEntity<?> counselWrite(@RequestBody @Valid Counsel counsel){
        try {
            counselService.createCounsel(counsel);
        } catch (Exception e){
            return ResponseEntity.badRequest().body("오류발생");
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/myCounsel")
    public ResponseEntity<Counsel> counselRead(@AuthenticationPrincipal UserDetails userDetails){
        try {
            return ResponseEntity.ok().body(counselService.readCounsel(userDetails.getUsername()));
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/counsel")
    public ResponseEntity<?> counselUpdate(@RequestBody CounselUpdateDTO counselUpdateDTO){
        try {
            counselService.updateCounsel(counselUpdateDTO);
        } catch (Exception e){
            return ResponseEntity.badRequest().body("오류발생");
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/counsel")
    public ResponseEntity<?> deleteCounsel(@RequestParam String counselId){
        try {
            counselService.deleteCounsel(counselId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

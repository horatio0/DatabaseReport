package com.example.databaseprogrammingreport.Controller;

import com.example.databaseprogrammingreport.Entity.Counsel;
import com.example.databaseprogrammingreport.Service.CounselService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CounselController {
    private final CounselService counselService;

    @PostMapping("/counsel")
    public ResponseEntity<?> counselWrite(@RequestBody Counsel counsel){
        return counselService.createCounsel(counsel);
    }

    @GetMapping("/myCounsel")
    public ResponseEntity<?> counselRead(@AuthenticationPrincipal UserDetails userDetails){
        return counselService.readCounsel(userDetails.getUsername());
    }

    @PutMapping("/counsel")
    public ResponseEntity<?> counselUpdate(@RequestBody Counsel counsel){
        return counselService.createCounsel(counsel);
    }

    @DeleteMapping("/counsel")
    public ResponseEntity<?> deleteCounsel(@RequestParam String counselId){
        return counselService.deleteCounsel(counselId);
    }
}

package com.example.databaseprogrammingreport.Service;

import com.example.databaseprogrammingreport.Entity.Counsel;
import com.example.databaseprogrammingreport.Repository.CounselRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CounselService {
    private final CounselRepository counselRepository;

    //C, U
    public ResponseEntity<?> createCounsel(Counsel counsel){
        try {
            counselRepository.save(counsel);
            return ResponseEntity.ok().build();
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    //R
    public ResponseEntity<?> readCounsel(String memberId){
        try {
            Counsel counsel = counselRepository.findById(memberId).get();
            return ResponseEntity.ok(counsel);
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    //D
    public ResponseEntity<?> deleteCounsel(String counselId){
        try {
            counselRepository.deleteById(counselId);
            return ResponseEntity.ok().build();
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
}

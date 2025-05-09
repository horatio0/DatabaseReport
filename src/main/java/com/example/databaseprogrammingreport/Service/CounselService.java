package com.example.databaseprogrammingreport.Service;

import com.example.databaseprogrammingreport.DTO.CounselUpdateDTO;
import com.example.databaseprogrammingreport.Entity.Counsel;
import com.example.databaseprogrammingreport.Repository.CounselRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CounselService {
    private final CounselRepository counselRepository;

    //C
    public void createCounsel(Counsel counsel) throws Exception{
        counselRepository.save(counsel);
    }

    //R
    public Counsel readCounsel(String memberId) throws Exception{
        return counselRepository.findById(memberId).get();
    }

    //U
    public void updateCounsel(CounselUpdateDTO counsel) throws Exception{
        Counsel dbCounsel = counselRepository.findById(counsel.getCounselId()).get();
        dbCounsel.setTopic(counsel.getTopic());
        dbCounsel.setContent(counsel.getContent());
        dbCounsel.setDate(counsel.getDate());
        dbCounsel.setType(counsel.getType());
        dbCounsel.setResult(counsel.getResult());
        dbCounsel.setClientStatus(counsel.getClientStatus());
        dbCounsel.setConfidential(counsel.isConfidential());

        counselRepository.save(dbCounsel);
    }

    //D
    public void deleteCounsel(String counselId) throws Exception{
        counselRepository.deleteById(counselId);
    }
}

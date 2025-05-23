package com.example.databaseprogrammingreport.Service;

import com.example.databaseprogrammingreport.DTO.CounselUpdateDTO;
import com.example.databaseprogrammingreport.DTO.MyCounselDTO;
import com.example.databaseprogrammingreport.Entity.Counsel;
import com.example.databaseprogrammingreport.Repository.CounselRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CounselService {
    private final CounselRepository counselRepository;
    private final MemberService memberService;

    //C
    public void createCounsel(Counsel counsel, String id) throws Exception{
        if (counselRepository.existsByDate(counsel.getDate())){
            Counsel c = counselRepository.findByDate(counsel.getDate());
            c.setTopic(counsel.getTopic());
            c.setContent(counsel.getContent());
            c.setDate(counsel.getDate());
            c.setType(counsel.getType());
            c.setResult(counsel.getResult());
            c.setClientStatus(counsel.getClientStatus());
            c.setConfidential(counsel.isConfidential());
        }else{
            counsel.setCounselorId(id);
            counselRepository.save(counsel);
        }
    }

    //R List
    public List<Counsel> readCounselList(String id) throws Exception{
        return counselRepository.findAllByCounselorId(id);
    }

    //Client read their history
    public List<MyCounselDTO> readClientHistory(String id) throws Exception{
        List<Counsel> counsels = counselRepository.findAllByClientId(id);
        List<MyCounselDTO> a = new ArrayList<>();

        counsels.forEach(c -> {
            MyCounselDTO m = MyCounselDTO.builder()
                    .title(c.getTopic())
                    .date(c.getDate())
                    .counselId(c.getCounselId())
                    .counselorName(memberService.getName(c.getCounselorId()))
                    .build();
            a.add(m);
        });
        return a;
    }

    //R
    public Counsel readCounsel(long counselId) throws Exception{
        return counselRepository.findById(counselId).get();
    }

//    //U
//    public void updateCounsel(CounselUpdateDTO counsel) throws Exception{
//        Counsel dbCounsel = counselRepository.findById(counsel.getCounselId()).get();
//        dbCounsel.setTopic(counsel.getTopic());
//        dbCounsel.setContent(counsel.getContent());
//        dbCounsel.setDate(counsel.getDate());
//        dbCounsel.setType(counsel.getType());
//        dbCounsel.setResult(counsel.getResult());
//        dbCounsel.setClientStatus(counsel.getClientStatus());
//        dbCounsel.setConfidential(counsel.isConfidential());
//
//        counselRepository.save(dbCounsel);
//    }

    //D
    public void deleteCounsel(long counselId) throws Exception{
        counselRepository.deleteById(counselId);
    }

    //보고서 작성 확인
    public Boolean haveCounselReport(int registerId) throws Exception{
        return counselRepository.existsCounselByRegisterId(registerId);
    }
}

package com.example.databaseprogrammingreport.Service;

import com.example.databaseprogrammingreport.Entity.Register;
import com.example.databaseprogrammingreport.Repository.RegisterRepository;
import com.example.databaseprogrammingreport.Repository.RegisterRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterService {
    private final RegisterRepository registerRepository;
    private final RegisterRequestRepository registerRequestRepository;

    //신청
    public void request(Register register) throws Exception{
        registerRequestRepository.save(register);
    }

    //수락
    public void register(int id) throws Exception{
        Register register = registerRequestRepository.findById(id).get();
        registerRequestRepository.deleteById(id);
        registerRepository.save(register);
    }

    //신청자 취소, 수락 전
    public void cancelRequestBeforeAccept(int id) throws Exception{
        registerRequestRepository.deleteById(id);
    }

    //상담원 취소


    //수정
}

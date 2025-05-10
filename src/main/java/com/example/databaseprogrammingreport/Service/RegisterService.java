package com.example.databaseprogrammingreport.Service;

import com.example.databaseprogrammingreport.DTO.RegisterUpdateDTO;
import com.example.databaseprogrammingreport.Entity.Message;
import com.example.databaseprogrammingreport.Entity.Register;
import com.example.databaseprogrammingreport.Repository.RegisterRepository;
import com.example.databaseprogrammingreport.Repository.RegisterRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RegisterService {
    private final RegisterRepository registerRepository;
    private final RegisterRequestRepository registerRequestRepository;
    private final MessageService messageService;
    private final MemberService memberService;

    //신청
    public void request(Register register) throws Exception{
        registerRequestRepository.save(register);

        String clientName = memberService.getName(register.getClientId());
        sendSystemMessage("새로운 상담 신청이 있습니다!",  clientName + "님께서 상담을 신청했습니다", register.getCounselorId() );
    }

    //수락
    public void register(int id) throws Exception{
        Register register = registerRequestRepository.findById(id).get();
        registerRequestRepository.deleteById(id);
        registerRepository.save(register);

        String counselorName = memberService.getName(register.getCounselorId());
        sendSystemMessage("상담 신청이 수락되었습니다.", counselorName+"님께 신청한 상담이 수락되었습니다.", register.getClientId());
    }

    //거절
    public void reject(int id, String reason) throws Exception{
        Register register = registerRequestRepository.findById(id).get();

        String counselorName = memberService.getName(register.getCounselorId());
        sendSystemMessage("상담 신청이 거절되었습니다.", counselorName+"님께 신청한 상담이 거절되었습니다.\n\n사유 : \n" + reason, register.getClientId());

        registerRequestRepository.deleteById(id);
    }

    //신청자 취소, 수락 전
    public void cancelRequestBeforeAccept(int id) throws Exception{
        registerRequestRepository.deleteById(id);
    }

    //신청자 취소, 수락 후
    public void cancelRequestAfterAccept(int id, String reason) throws Exception{
        Register register = registerRepository.findById(id).get();

        String clientName = memberService.getName(register.getClientId());
        sendSystemMessage("상담 신청이 취소되었습니다.", clientName+"님께서 신청한 상담을 취소하였습니다.\n\n사유 : \n" + reason, register.getCounselorId());

        registerRepository.deleteById(id);
    }

    //상담원 취소
    public void cancelCounsel(int id, String reason) throws Exception{
        Register register = registerRepository.findById(id).get();

        String counselorName = memberService.getName(register.getCounselorId());
        sendSystemMessage("상담 신청이 취소되었습니다.", counselorName+"님께서 신청한 상담을 취소하였습니다.\n\n사유 : \n" + reason, register.getClientId());

        registerRepository.deleteById(id);
    }

    //수정
    public void update(int id, RegisterUpdateDTO registerDTO) throws Exception{
        Register register = registerRepository.findById(id).get();
        register.setYear(registerDTO.getYear());
        register.setMonth(registerDTO.getMonth());
        register.setDay(registerDTO.getDay());
        register.setHour(registerDTO.getHour());
        register.setMinute(registerDTO.getMinute());
        register.setRequest(registerDTO.getRequest());
        //쪽지 발송
        String clientName = memberService.getName(register.getClientId());
        sendSystemMessage("상담 신청이 변경되었습니다.", clientName+"님께서 신청한 상담을 변경하였습니다.", register.getCounselorId());
    }

    //시스템 메세지 발송
    public void sendSystemMessage(String title, String content, String receiverId) throws Exception{
        Message message = new Message();
        message = Message.builder()
                .senderId("System")
                .receiverId(receiverId)
                .title(title)
                .content(content)
                .build();
        messageService.send(message);
    }
}

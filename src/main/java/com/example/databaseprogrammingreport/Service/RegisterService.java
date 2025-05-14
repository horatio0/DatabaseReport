package com.example.databaseprogrammingreport.Service;

import com.example.databaseprogrammingreport.DTO.RegisterUpdateDTO;
import com.example.databaseprogrammingreport.DTO.WeekScheduleDTO;
import com.example.databaseprogrammingreport.Entity.Message;
import com.example.databaseprogrammingreport.Entity.Register;
import com.example.databaseprogrammingreport.Repository.RegisterRepository;
import com.example.databaseprogrammingreport.Repository.RegisterRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

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
        register.setClientName(memberService.getName(register.getClientId()));
        registerRequestRepository.save(register);

        sendSystemMessage("새로운 상담 신청이 있습니다!",  register.getClientName() + "님께서 상담을 신청했습니다", register.getCounselorId() );
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

        sendSystemMessage("상담 신청이 취소되었습니다.", register.getClientName()+"님께서 신청한 상담을 취소하였습니다.\n\n사유 : \n" + reason, register.getCounselorId());

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
        sendSystemMessage("상담 신청이 변경되었습니다.", register.getClientName()+"님께서 신청한 상담을 변경하였습니다.", register.getCounselorId());
    }

    //1주 예약 현황
    public List<WeekScheduleDTO> readSchedule(String id) throws Exception{
        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate weekEnd = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        if (weekStart.isAfter(weekEnd)) {
            throw new IllegalArgumentException("시작일이 종료일보다 늦을 수 없습니다.");
        }


        List<Register> registers = registerRepository.findByWeekPeriod(
                id, weekStart.getYear(), weekStart.getMonthValue(), weekStart.getDayOfMonth(),
                weekEnd.getYear(), weekEnd.getMonthValue(), weekEnd.getDayOfMonth()
        );
        List<WeekScheduleDTO> schedules = new ArrayList<>();

        for(Register register : registers){
            WeekScheduleDTO a = WeekScheduleDTO.builder()
                    .clientName(register.getClientName())
                    .year(register.getYear())
                    .month(register.getMonth())
                    .day(register.getDay())
                    .hour(register.getHour())
                    .minute(register.getMinute())
                    .build();
            schedules.add(a);
        }

        return schedules;
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

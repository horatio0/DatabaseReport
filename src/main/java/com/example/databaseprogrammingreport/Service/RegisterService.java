package com.example.databaseprogrammingreport.Service;

import com.example.databaseprogrammingreport.DTO.MyRegisterListDTO;
import com.example.databaseprogrammingreport.DTO.RegisterUpdateDTO;
import com.example.databaseprogrammingreport.DTO.WeekScheduleDTO;
import com.example.databaseprogrammingreport.Entity.Message;
import com.example.databaseprogrammingreport.Entity.Register;
import com.example.databaseprogrammingreport.Entity.DayScheduleDTO;
import com.example.databaseprogrammingreport.Entity.RegisterRequest;
import com.example.databaseprogrammingreport.Repository.RegisterRepository;
import com.example.databaseprogrammingreport.Repository.RegisterRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RegisterService {
    private final RegisterRepository registerRepository;
    private final RegisterRequestRepository registerRequestRepository;
    private final MessageService messageService;
    private final MemberService memberService;
    private final CounselService counselService;

    //신청
    public void request(RegisterRequest register, String clientId) throws Exception{
        register.setClientId(clientId);
        register.setClientName(memberService.getName(clientId));
        registerRequestRepository.save(register);

        sendSystemMessage("새로운 상담 신청이 있습니다!",  register.getClientName() + "님께서 상담을 신청했습니다", register.getCounselorId(), register.getClientId());
    }

    //신청한 상담 목록
    public List<MyRegisterListDTO> requestList(String id) throws Exception{
        List<Register> registers = registerRepository.findAllByClientId(id);
        List<RegisterRequest> registers1 = registerRequestRepository.findAllByClientId(id);
        List<MyRegisterListDTO> a = new ArrayList<>();

        for (Register r : registers) {
            MyRegisterListDTO dto = MyRegisterListDTO.builder()
                    .counselorName(memberService.getName(r.getCounselorId()))
                    .registerId(r.getRegisterId())
                    .date(r.getYear() + "." + r.getMonth() + "." + r.getDay())
                    .isAccepted(true)
                    .haveCounselReport(counselService.haveCounselReport(r.getRegisterId()))
                    .build();
            a.add(dto);
        }
        for (RegisterRequest r : registers1) {
            MyRegisterListDTO dto = MyRegisterListDTO.builder()
                    .registerId(r.getRegisterId())
                    .counselorName(memberService.getName(r.getCounselorId()))
                    .date(r.getYear() + "." + r.getMonth() + "." + r.getDay())
                    .isAccepted(false)
                    .haveCounselReport(counselService.haveCounselReport(r.getRegisterId()))
                    .build();
            a.add(dto);
        }
        return a;
    }

    //신청받은 상담 목록
    public List<RegisterRequest> getRequests(String id) throws Exception{
        return registerRequestRepository.findAllByCounselorId(id);
    }

    //수락
    public void register(int id) throws Exception{
        RegisterRequest r= registerRequestRepository.findById(id).get();
        registerRequestRepository.deleteById(id);
        Register register = Register.builder()
                .registerId(r.getRegisterId())
                .counselorId(r.getCounselorId())
                .clientId(r.getClientId())
                .clientName(r.getClientName())
                .year(r.getYear())
                .month(r.getMonth())
                .day(r.getDay())
                .hour(r.getHour())
                .minute(r.getMinute())
                .request(r.getRequest())
                .build();
        registerRepository.save(register);

        String counselorName = memberService.getName(register.getCounselorId());
        sendSystemMessage("상담 신청이 수락되었습니다.", counselorName+"님께 신청한 상담이 수락되었습니다.", register.getClientId(), register.getCounselorId());
    }

    //거절
    public void reject(int id, String reason) throws Exception{
        RegisterRequest register = registerRequestRepository.findById(id).get();

        String counselorName = memberService.getName(register.getCounselorId());
        sendSystemMessage("상담 신청이 거절되었습니다.", counselorName+"님께 신청한 상담이 거절되었습니다.\n\n사유 : \n" + reason, register.getClientId(), register.getCounselorId());

        registerRequestRepository.deleteById(id);
    }

    //신청자 취소, 수락 전
    public void cancelRequestBeforeAccept(int id) throws Exception{
        registerRequestRepository.deleteById(id);
    }

    //신청자 취소, 수락 후
    public void cancelRequestAfterAccept(int id, String reason) throws Exception{
        Register register = registerRepository.findById(id).get();

        sendSystemMessage("상담 신청이 취소되었습니다.", register.getClientName()+"님께서 신청한 상담을 취소하였습니다.\n\n사유 : \n" + reason, register.getCounselorId(), register.getClientId());

        registerRepository.deleteById(id);
    }

    //상담원 취소
    public void cancelCounsel(int id, String reason) throws Exception{
        Register register = registerRepository.findById(id).get();

        String counselorName = memberService.getName(register.getCounselorId());
        sendSystemMessage("상담 신청이 취소되었습니다.", counselorName+"님께서 신청한 상담을 취소하였습니다.\n\n사유 : \n" + reason, register.getClientId(), register.getCounselorId());

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
        sendSystemMessage("상담 신청이 변경되었습니다.", register.getClientName()+"님께서 신청한 상담을 변경하였습니다.", register.getCounselorId(), register.getClientId());
    }

    //일간 예약 현황
    public List<DayScheduleDTO> readDaySchedules(String id) throws Exception{
        LocalDate today = LocalDate.now();
        List<Register> a = registerRepository.findAllByCounselorIdAndYearAndMonthAndDay(id, today.getYear(), today.getMonthValue(), today.getDayOfMonth());
        List<DayScheduleDTO> schedules = new ArrayList<>();

        for (Register register : a) {
            schedules.add(DayScheduleDTO.builder()
                            .hour(register.getHour())
                            .minute(register.getMinute())
                            .clientName(register.getClientName())
                    .build());
        }
        return schedules;
    }

    //수락한 상담 목록
    public Map<String, String> getRegisters(String id) throws Exception{
        List<Register> a = registerRepository.findAllByCounselorId(id);
        Map<String, String> registers = new HashMap<>();

        for (Register r : a) {
            String s = r.getYear() + "." + r.getMonth() + "." + r.getDay() + " " + r.getHour() + ":" + r.getMinute() + "   " + r.getClientName();
            registers.put(s, r.getClientId() + "-" + r.getRegisterId());
        }

        return registers;
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
    public void sendSystemMessage(String title, String content, String receiverId, String sender) throws Exception{
        Message message = new Message();
        message = Message.builder()
                .senderId("System")
                .receiverId(receiverId)
                .title(title)
                .content(content)
                .build();
        messageService.send(message, sender);
    }
}

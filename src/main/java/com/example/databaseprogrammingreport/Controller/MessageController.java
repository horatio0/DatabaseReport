package com.example.databaseprogrammingreport.Controller;

import com.example.databaseprogrammingreport.Entity.Message;
import com.example.databaseprogrammingreport.Service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    //발송
    @PostMapping("/message")
    public ResponseEntity<?> send(@RequestBody @Valid Message message) throws Exception{
        try {
            messageService.send(message);
            return ResponseEntity.ok().build();
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    //수신 목록
    @GetMapping("/messages")
    public ResponseEntity<?> getMessages(@RequestParam String receiverId){
        try {
            return ResponseEntity.ok().body(messageService.getMessages(receiverId));
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    //메세지 확인
    @GetMapping("/message")
    public ResponseEntity<?> getMessage(@RequestParam int id){
        try {
            return ResponseEntity.ok().body(messageService.getMessage(id));
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    //삭제
    @DeleteMapping("/message")
    public ResponseEntity<?> delete(@RequestParam int id){
        try {
            messageService.delete(id);
            return ResponseEntity.ok().build();
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    //새로운 쪽지
    @GetMapping("/message/new")
    public ResponseEntity<Boolean> isNewMessage(@RequestParam String receiverId){
        try {
            return ResponseEntity.ok().body(messageService.isNewMessage(receiverId));
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
}

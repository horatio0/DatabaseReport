package com.example.databaseprogrammingreport.Controller;

import com.example.databaseprogrammingreport.Entity.Message;
import com.example.databaseprogrammingreport.Service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    //발송
    @PostMapping("/message")
    public ResponseEntity<?> send(@RequestBody @Valid Message message, @AuthenticationPrincipal UserDetails userDetails) throws Exception{
        try {
            messageService.send(message, userDetails.getUsername());
            return ResponseEntity.ok().build();
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    //수신 목록
    @GetMapping("/messages")
    public ResponseEntity<?> getMessages(@AuthenticationPrincipal UserDetails userDetails){
        try {
            return ResponseEntity.ok().body(messageService.getMessages(userDetails.getUsername()));
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    //메세지 확인
//    @GetMapping("/message")
//    public ResponseEntity<?> getMessage(@RequestParam long id){
//        try {
//            return ResponseEntity.ok().body(messageService.getMessage(id));
//        } catch (Exception e){
//            return ResponseEntity.badRequest().build();
//        }
//    }

    @PutMapping("/message")
    public ResponseEntity<?> read(@RequestParam long id){
        messageService.read(id);
        return ResponseEntity.ok().build();
    }

    //삭제
    @DeleteMapping("/message")
    public ResponseEntity<?> delete(@RequestParam long id){
        try {
            messageService.delete(id);
            return ResponseEntity.ok().build();
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    //새로운 쪽지
    @GetMapping("/message/new")
    public ResponseEntity<Boolean> isNewMessage(@AuthenticationPrincipal UserDetails userDetails){
        try {
            return ResponseEntity.ok().body(messageService.isNewMessage(userDetails.getUsername()));
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
}

package com.example.databaseprogrammingreport.Service;

import com.example.databaseprogrammingreport.Entity.Message;
import com.example.databaseprogrammingreport.Repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;

    //발송
    public void send(Message message) throws Exception{
        message.setIsRead(Boolean.FALSE);
        messageRepository.save(message);
    }

    //수신 목록
    public List<Message> getMessages(String receiverId){
        return messageRepository.findAllByReceiverId(receiverId);
    }

    //메세지 확인
    public Message getMessage(int id){
        Message message = messageRepository.findById(id).get();
        message.setIsRead(Boolean.TRUE);
        return message;
    }

    //삭제
    public void delete(int id){
        messageRepository.deleteById(id);
    }

    //새로온 쪽지
    public boolean isNewMessage(String receiverId){
        return messageRepository.existsByReceiverIdAndIsRead(receiverId, Boolean.FALSE);
    }
}

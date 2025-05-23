package com.example.databaseprogrammingreport.Repository;

import com.example.databaseprogrammingreport.Entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByReceiverId(String receiverId);
    Boolean existsByReceiverIdAndIsRead(String receiverId, Boolean isRead);
}

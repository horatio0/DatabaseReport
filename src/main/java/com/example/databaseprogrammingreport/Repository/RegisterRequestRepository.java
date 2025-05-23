package com.example.databaseprogrammingreport.Repository;

import com.example.databaseprogrammingreport.Entity.Register;
import com.example.databaseprogrammingreport.Entity.RegisterRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegisterRequestRepository extends JpaRepository<RegisterRequest, Integer> {
    List<RegisterRequest> findAllByCounselorId(String counselorId);

    List<RegisterRequest> findAllByClientId(String clientId);
}

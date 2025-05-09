package com.example.databaseprogrammingreport.Repository;

import com.example.databaseprogrammingreport.Entity.Register;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegisterRequestRepository extends JpaRepository<Register, Integer> {
}

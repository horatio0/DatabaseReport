package com.example.databaseprogrammingreport.Repository;

import com.example.databaseprogrammingreport.Entity.Introduction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IntroductionRepository extends JpaRepository<Introduction, String> {
    List<Introduction> findAllByExpertise(String expertise);
}

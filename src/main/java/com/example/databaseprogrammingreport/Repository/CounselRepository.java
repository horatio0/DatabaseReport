package com.example.databaseprogrammingreport.Repository;

import com.example.databaseprogrammingreport.Entity.Counsel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CounselRepository extends JpaRepository<Counsel, Long> {
    List<Counsel> findAllByCounselorId(String counselorId);

    boolean existsCounselByRegisterId(int registerId);

    List<Counsel> findAllByClientId(String clientId);

    boolean existsByDate(String date);

    Counsel findByDate(String date);
}

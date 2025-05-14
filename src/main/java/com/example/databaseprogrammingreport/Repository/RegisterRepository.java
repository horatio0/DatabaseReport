package com.example.databaseprogrammingreport.Repository;

import com.example.databaseprogrammingreport.Entity.Register;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RegisterRepository extends JpaRepository<Register, Integer> {
    @Query("SELECT r FROM Register r WHERE " +
            "r.counselorId = :id AND (" +
            "(r.year = :year1 AND r.month = :month1 AND r.day >= :day1) OR " +
            "(r.year = :year2 AND r.month = :month2 AND r.day <= :day2) OR " +
            "((r.year = :year1 AND r.month > :month1) OR " +
            "(r.year > :year1 AND r.year < :year2) OR " +
            "(r.year = :year2 AND r.month < :month2))" +
            ") ORDER BY r.year, r.month, r.day, r.hour, r.minute")

    List<Register> findByWeekPeriod(
            @Param("id") String counselorId,
            @Param("year1") int startYear, @Param("month1") int startMonth, @Param("day1") int startDay,
            @Param("year2") int endYear, @Param("month2") int endMonth, @Param("day2") int endDay
    );

}

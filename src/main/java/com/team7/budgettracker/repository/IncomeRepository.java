package com.team7.budgettracker.repository;

import com.team7.budgettracker.model.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Integer> {

    @Query("SELECT e FROM Income e WHERE e.datePlanned BETWEEN :startDate AND :endDate")
    Optional<List<Income>> findAllByLocalDatePlannedBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

}

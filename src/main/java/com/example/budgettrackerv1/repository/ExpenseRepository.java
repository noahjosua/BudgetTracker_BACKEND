package com.example.budgettrackerv1.repository;

import com.example.budgettrackerv1.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Integer> {

    @Query("SELECT e FROM Expense e WHERE e.datePlanned BETWEEN :startDate AND :endDate")
    Optional<List<Expense>> findAllByLocalDatePlannedBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

}

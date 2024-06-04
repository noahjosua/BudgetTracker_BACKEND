package com.example.budgettrackerv1.repository;

import com.example.budgettrackerv1.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Integer> {

    //Optional<List<Expense>> findAllByDateCreatedBetween(java.util.Date dateCreated, java.util.Date dateCreated2);
}

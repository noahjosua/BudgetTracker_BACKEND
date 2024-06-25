package com.team7.budgettracker.model;

import java.time.LocalDate;

public interface Entry {
    LocalDate getDateCreated();
    LocalDate getDatePlanned();
    Category getCategory();
    double getAmount();
    String getDescription();
}

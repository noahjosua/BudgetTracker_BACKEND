package com.team7.budgettracker.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "income")
public class Income implements Entry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Integer id;

    @Column(name = "date_created", nullable = false)
    private LocalDate dateCreated;

    @Column(name = "date_planned", nullable = false)
    private LocalDate datePlanned;

    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "amount", nullable = false)
    private double amount;

    @SuppressWarnings("unused")
    public Integer getId() {
        return id;
    }

    @SuppressWarnings("unused")
    public void setId(Integer id) {
        this.id = id;
    }

    @SuppressWarnings("unused")
    public LocalDate getDateCreated() {
        return dateCreated;
    }

    @SuppressWarnings("unused")
    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    @SuppressWarnings("unused")
    public LocalDate getDatePlanned() {
        return datePlanned;
    }

    @SuppressWarnings("unused")
    public void setDatePlanned(LocalDate datePlanned) {
        this.datePlanned = datePlanned;
    }

    @SuppressWarnings("unused")
    public Category getCategory() {
        return category;
    }

    @SuppressWarnings("unused")
    public void setCategory(Category category) {
        this.category = category;
    }

    @SuppressWarnings("unused")
    public String getDescription() {
        return description;
    }

    @SuppressWarnings("unused")
    public void setDescription(String description) {
        this.description = description;
    }

    @SuppressWarnings("unused")
    public double getAmount() {
        return amount;
    }

    @SuppressWarnings("unused")
    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Income income = (Income) o;
        return Double.compare(amount, income.amount) == 0 && Objects.equals(id, income.id) && Objects.equals(dateCreated, income.dateCreated) && Objects.equals(datePlanned, income.datePlanned) && category == income.category && Objects.equals(description, income.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateCreated, datePlanned, category, description, amount);
    }

    @Override
    public String toString() {
        return "Income{" +
                "id=" + id +
                ", dateCreated='" + dateCreated + '\'' +
                ", datePlanned='" + datePlanned + '\'' +
                ", category=" + category +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                '}';
    }
}

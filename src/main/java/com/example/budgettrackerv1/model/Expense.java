package com.example.budgettrackerv1.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "expense")
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false)
    private Integer id;

    @Column(name = "date_created", nullable = false)
    private Date dateCreated;

    @Column(name = "date_planned", nullable = false)
    private Date datePlanned;

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
    public Date getDateCreated() {
        return dateCreated;
    }

    @SuppressWarnings("unused")
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    @SuppressWarnings("unused")
    public Date getDatePlanned() {
        return datePlanned;
    }

    @SuppressWarnings("unused")
    public void setDatePlanned(Date datePlanned) {
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
    public String toString() {
        return "Expense{" +
                "id=" + id +
                ", dateCreated=" + dateCreated +
                ", datePlanned=" + datePlanned +
                ", category=" + category +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                '}';
    }
}

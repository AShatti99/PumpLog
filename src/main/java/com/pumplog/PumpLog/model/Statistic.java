package com.pumplog.PumpLog.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 *  Stitische per esercizio. Contiene la data, il massimo peso e l'esercizio a cui si riferisce
 */
@Entity
@Table(name = "STATISTIC")
@Getter
@Setter
public class Statistic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "DATE")
    private LocalDate date;

    @Column(name = "MAX_WEIGHT")
    private float max_weight;

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    @Override
    public String toString() {
        return String.format("Statistic{id=%d, date=%s, max_weight=%f, exercise='%s'}",
                id, date, max_weight, exercise.getName());
    }
}

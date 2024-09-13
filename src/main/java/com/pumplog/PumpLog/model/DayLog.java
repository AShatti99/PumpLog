package com.pumplog.PumpLog.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 *  Risultati ottenuti per esercizio. Contiene: la data del log, l'esercizio a cui si riferisce
 */
@Entity
@Table(name = "DAY_LOG")
@Getter
@Setter
public class DayLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "DATE")
    private LocalDate date;

    @Column(name = "DAILY_LOG")
    private String dailyLog;

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    @Override
    public String toString() {
        return String.format("DayLog{id=%d, date=%s, dailyLog='%s', exercise='%s'}",
                id, date, dailyLog, exercise.getName());
    }
}

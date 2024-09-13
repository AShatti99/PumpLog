package com.pumplog.PumpLog.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Singolo esercizio contiene: nome dell'esercizio, il numero di serie, il tempo di recupero, le tabella associate, i log ottenuti per quell'esercizio e le statistiche associate.
 * Un esercizio può essere inserito in piu' schede
 */
@Entity
@Table(name = "EXERCISE")
@Getter
@Setter
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "SETS")
    private String sets;

    @Column(name = "BREAK")
    private String breakDuration;

    @ManyToMany(mappedBy = "exerciseList")
    private List<WorkoutPlan> workoutPlanList;

    @OneToMany(mappedBy = "exercise") // nome del campo nella classe DayLog
    private List<DayLog> dayLogList;

    @OneToMany(mappedBy = "exercise") // nome del campo nella classe Statistic
    private List<Statistic> statisticList;

    @Override
    public String toString() {
        String workoutPlanNames = workoutPlanList.stream()
                .map(WorkoutPlan::getName)
                .collect(Collectors.joining(", "));

        String dayLogDates = dayLogList.stream()
                .map(dayLog -> dayLog.getDate().toString())
                .collect(Collectors.joining(", "));

        return String.format("Exercise{id=%d, name='%s', sets='%s', breakDuration='%s', workoutPlans=[%s], dayLogs=[%s]}",
                id, name, sets, breakDuration, workoutPlanNames, dayLogList != null ? dayLogDates : "N/A");
    }
}

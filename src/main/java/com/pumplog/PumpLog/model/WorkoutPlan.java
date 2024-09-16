package com.pumplog.PumpLog.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Scheda d'allenamento contiene: nome della scheda, la data di inizio e fine, lo user proprietario della scheda ed una lista di esercizi
 */
@Entity
@Table(name = "WORKOUT_PLAN")
@Getter
@Setter
public class WorkoutPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "START_DATE")
    private LocalDate startDate;

    @Column(name = "END_DATE")
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "EXERCISE_WORKOUT_PLAN",
            joinColumns = @JoinColumn(name = "WORKOUT_PLAN_ID"),
            inverseJoinColumns = @JoinColumn(name = "EXERCISE_ID"))
    private List<Exercise> exerciseList;

    @Override
    public String toString() {
        String exerciseNames = "N/A";

        if (!CollectionUtils.isEmpty(exerciseList)) {
            exerciseNames = exerciseList.stream()
                    .map(Exercise::getName)
                    .collect(Collectors.joining(", "));
        }

        return String.format("WorkoutPlan{id=%d, name='%s', startDate=%s, endDate=%s, user='%s', exercises=[%s]}",
                id, name, startDate, endDate, user.getUsername(), exerciseNames);
    }
}

package com.pumplog.PumpLog.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

/**
 *  Utenti dell'applicazione
 */
@Entity
@Table(name = "USERS")  // la parola 'USER' e' una parola riservata nel DB. Consigliato chiamare la tabella con @Table(name = "USERS") in modo da non ricevere errore
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PASSWORD")
    private String password;

    @OneToMany(mappedBy = "user")  // nome del campo nella classe WorkoutPlan
    private List<WorkoutPlan> workoutPlanList;

    @Override
    public String toString() {
        String workoutPlansNames = workoutPlanList.stream()
                .map(WorkoutPlan::getName)
                .collect(Collectors.joining(", "));

        return String.format("User{id=%d, username='%s', email='%s', password='%s', workoutPlans=[%s]}",
                id, username, email, password, workoutPlanList != null ? workoutPlansNames : "N/A");
    }
}

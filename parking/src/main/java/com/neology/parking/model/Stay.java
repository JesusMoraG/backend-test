package com.neology.parking.model;

import jakarta.persistence.*;
import java.util.Calendar;


@Entity
@Table(name = "stays")
public class Stay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String plate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "entry_time", nullable = false)
    private Calendar entryTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "exit_time")
    private Calendar exitTime;

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public Calendar getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(Calendar entryTime) {
        this.entryTime = entryTime;
    }

    public Calendar getExitTime() {
        return exitTime;
    }

    public void setExitTime(Calendar exitTime) {
        this.exitTime = exitTime;
    }
}

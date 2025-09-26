package com.neology.parking.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "resident_vehicles")
public class ResidentVehicle implements Serializable {

    @Id
    private String plate;

    private int accumulatedMinutes = 0;

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public int getAccumulatedMinutes() {
        return accumulatedMinutes;
    }

    public void setAccumulatedMinutes(int accumulatedMinutes) {
        this.accumulatedMinutes = accumulatedMinutes;
    }
}

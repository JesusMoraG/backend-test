package com.neology.parking.strategy;

import org.springframework.stereotype.Component;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;


@Component
public class NonResidentVehicleHandler implements VehicleType {
    private static final double MINUTE_RATE = 3.0;

    @Override
    public String calculateStay(Calendar entryTime, Calendar exitTime) {
        long diffInMillis = exitTime.getTimeInMillis() - entryTime.getTimeInMillis();
        long minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis);
        double totalCost = minutes * MINUTE_RATE;
        return String.format("El importe a pagar por la estancia es de MXN$ %.2f", totalCost);
    }

    @Override
    public String getVehicleType() {
        return "No Residente";
    }
}

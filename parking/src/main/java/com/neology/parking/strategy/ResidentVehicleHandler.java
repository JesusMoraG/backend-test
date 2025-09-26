package com.neology.parking.strategy;

import org.springframework.stereotype.Component;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

@Component
public class ResidentVehicleHandler implements VehicleType {
    @Override
    public String calculateStay(Calendar entryTime, Calendar exitTime) {
        long diffInMillis = exitTime.getTimeInMillis() - entryTime.getTimeInMillis();
        long minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis);
        return String.format("Estancia de vehículo residente registrada. Tiempo acumulado: %d minutos.", minutes);
    }

    @Override
    public String getVehicleType() {
        return "Residente";
    }
}

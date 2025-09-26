package com.neology.parking.strategy;

import org.springframework.stereotype.Component;
import java.util.Calendar;


@Component
public class OfficialVehicleHandler implements VehicleType {
    @Override
    public String calculateStay(Calendar entryTime, Calendar exitTime) {
        return "Estancia de vehículo oficial registrada sin costo.";
    }

    @Override
    public String getVehicleType() {
        return "Oficial";
    }
}

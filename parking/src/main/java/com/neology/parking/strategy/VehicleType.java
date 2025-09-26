package com.neology.parking.strategy;

import java.util.Calendar;

public interface VehicleType {

    String calculateStay(Calendar entryTime, Calendar exitTime);

    String getVehicleType();
}

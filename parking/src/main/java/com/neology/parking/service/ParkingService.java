package com.neology.parking.service;

import com.neology.parking.model.ResidentVehicle;
import com.neology.parking.model.Stay;
import com.neology.parking.repository.ResidentVehicleRepository;
import com.neology.parking.repository.StayRepository;
import com.neology.parking.strategy.VehicleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ParkingService {

    private final StayRepository stayRepository;
    private final ResidentVehicleRepository residentVehicleRepository;
    private final Map<String, VehicleType> vehicleTypeHandlers;

    private final Map<String, String> registeredVehicles = new HashMap<>();

    @Autowired
    public ParkingService(StayRepository stayRepository, ResidentVehicleRepository residentVehicleRepository, List<VehicleType> vehicleTypeHandlers) {
        this.stayRepository = stayRepository;
        this.residentVehicleRepository = residentVehicleRepository;
        this.vehicleTypeHandlers = vehicleTypeHandlers.stream()
                .collect(Collectors.toMap(VehicleType::getVehicleType, Function.identity()));
    }

    public Stay registerEntry(String plate) {
        Stay stay = new Stay();
        stay.setPlate(plate);
        stay.setEntryTime(Calendar.getInstance());
        return stayRepository.save(stay);
    }

    public String registerExit(String plate) {
        Stay stay = stayRepository.findFirstByPlateAndExitTimeIsNullOrderByEntryTimeDesc(plate)
                .orElse(null);
        if (stay == null) {
            return "Error: No se encontró una estancia activa para el vehículo con placa " + plate;
        }

        stay.setExitTime(Calendar.getInstance());
        stayRepository.save(stay);

     
        String vehicleType = getVehicleType(plate);
        VehicleType handler = vehicleTypeHandlers.get(vehicleType);

        if (handler != null) {
           
            if ("Residente".equals(vehicleType)) {
                int minutes = difEnMinutos(stay.getEntryTime(), stay.getExitTime());

                ResidentVehicle resident = residentVehicleRepository.findById(plate)
                        .orElse(new ResidentVehicle());
                resident.setPlate(plate);
                resident.setAccumulatedMinutes(resident.getAccumulatedMinutes() + minutes);
                residentVehicleRepository.save(resident);
            }
            return handler.calculateStay(stay.getEntryTime(), stay.getExitTime());
        } else {
            return "Tipo de vehículo desconocido: " + vehicleType;
        }
    }


    public String registerOfficialVehicle(String plate) {
        registeredVehicles.put(plate, "Oficial");
        return "Vehículo oficial con placa " + plate + " registrado exitosamente.";
    }

    public String registerResidentVehicle(String plate) {
        ResidentVehicle resident = new ResidentVehicle();
        resident.setPlate(plate);
        resident.setAccumulatedMinutes(0);
        residentVehicleRepository.save(resident);
        return "Vehículo residente con placa " + plate + " registrado exitosamente.";
    }


    private String getVehicleType(String plate) {
        if (registeredVehicles.containsKey(plate)) {
            return registeredVehicles.get(plate);
        } else if (residentVehicleRepository.existsById(plate)) {
            return "Residente";
        } else {
            return "No Residente";
        }
    }

    public String startMonth() {
        stayRepository.deleteAll();
        List<ResidentVehicle> residents = residentVehicleRepository.findAll();
        for (ResidentVehicle resident : residents) {
            resident.setAccumulatedMinutes(0);
            residentVehicleRepository.save(resident);
        }
        return "Proceso de 'Comienza mes' completado exitosamente.";
    }

    public String generateResidentReport() {
        List<ResidentVehicle> residents = residentVehicleRepository.findAll();
        StringBuilder report = new StringBuilder();
        report.append("Núm. placa\t\tTiempo estacionado (min.)\t\tCantidad a pagar\n");

        for (ResidentVehicle resident : residents) {
            double amountToPay = resident.getAccumulatedMinutes() * 0.05;
            report.append(String.format("%s\t\t\t%d\t\t\t\t\t\t\t%.2f\n",
                    resident.getPlate(),
                    resident.getAccumulatedMinutes(),
                    amountToPay));
        }

        return report.toString();
    }
    

    private int difEnMinutos(Calendar initial, Calendar finalDate) {
        long milis1 = initial.getTimeInMillis();
        long milis2 = finalDate.getTimeInMillis();
        long diff = milis2 - milis1;
        return (int) (diff / (60 * 1000));
    }
}

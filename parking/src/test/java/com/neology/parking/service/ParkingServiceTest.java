package com.neology.parking.service;

import com.neology.parking.model.ResidentVehicle;
import com.neology.parking.model.Stay;
import com.neology.parking.repository.ResidentVehicleRepository;
import com.neology.parking.repository.StayRepository;
import com.neology.parking.strategy.NonResidentVehicleHandler;
import com.neology.parking.strategy.OfficialVehicleHandler;
import com.neology.parking.strategy.ResidentVehicleHandler;
import com.neology.parking.strategy.VehicleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

    @Mock
    private StayRepository stayRepository;

    @Mock
    private ResidentVehicleRepository residentVehicleRepository;

    @InjectMocks
    private ParkingService parkingService;

    @BeforeEach
    public void setUp() {
        List<VehicleType> vehicleHandlers = Arrays.asList(
                new OfficialVehicleHandler(),
                new ResidentVehicleHandler(),
                new NonResidentVehicleHandler()
        );
        parkingService = new ParkingService(stayRepository, residentVehicleRepository, vehicleHandlers);
    }

    @Test
    void testRegisterEntry() {
        String plate = "TEST001";
        Stay mockStay = new Stay();
        mockStay.setPlate(plate);
        Mockito.when(stayRepository.save(Mockito.any(Stay.class))).thenReturn(mockStay);

        Stay savedStay = parkingService.registerEntry(plate);

        assertEquals(plate, savedStay.getPlate());
    }

    @Test
    void testRegisterExitNonResident() {
        String plate = "TEST002";
        Calendar entryTime = Calendar.getInstance();
        entryTime.add(Calendar.MINUTE, -10); // Resta 10 minutos
        Stay mockStay = new Stay();
        mockStay.setPlate(plate);
        mockStay.setEntryTime(entryTime);

        Mockito.when(stayRepository.findFirstByPlateAndExitTimeIsNullOrderByEntryTimeDesc(plate))
                .thenReturn(Optional.of(mockStay));
        Mockito.when(stayRepository.save(Mockito.any(Stay.class))).thenReturn(mockStay);

        String result = parkingService.registerExit(plate);

        assertTrue(result.contains("El importe a pagar por la estancia es de MXN$"));
    }

    @Test
    void testRegisterExitOfficialVehicle() {
        String plate = "TEST003";
        parkingService.registerOfficialVehicle(plate);
        Calendar entryTime = Calendar.getInstance();
        entryTime.add(Calendar.MINUTE, -20); // Resta 20 minutos
        Stay mockStay = new Stay();
        mockStay.setPlate(plate);
        mockStay.setEntryTime(entryTime);

        Mockito.when(stayRepository.findFirstByPlateAndExitTimeIsNullOrderByEntryTimeDesc(plate))
                .thenReturn(Optional.of(mockStay));
        Mockito.when(stayRepository.save(Mockito.any(Stay.class))).thenReturn(mockStay);

        String result = parkingService.registerExit(plate);

        assertTrue(result.contains("Estancia de vehículo oficial registrada"));
    }

    @Test
    void testRegisterExitNoActiveStayFound() {
        String plate = "TEST004";
        Mockito.when(stayRepository.findFirstByPlateAndExitTimeIsNullOrderByEntryTimeDesc(plate))
                .thenReturn(Optional.empty());

        String result = parkingService.registerExit(plate);

        assertEquals("Error: No se encontró una estancia activa para el vehículo con placa TEST004", result);
    }
}

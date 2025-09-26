package com.neology.parking.controller;

import com.neology.parking.model.Stay;
import com.neology.parking.service.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/parking")
public class ParkingController {

    private final ParkingService parkingService;

    @Autowired
    public ParkingController(ParkingService parkingService) {
        this.parkingService = parkingService;
    }

   
    @PostMapping("/entry/{plate}")
    public ResponseEntity<Stay> registerEntry(@PathVariable String plate) {
        Stay stay = parkingService.registerEntry(plate);
        return new ResponseEntity<>(stay, HttpStatus.CREATED);
    }

   
    @PostMapping("/exit/{plate}")
    public ResponseEntity<String> registerExit(@PathVariable String plate) {
        String result = parkingService.registerExit(plate);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

   
    @PostMapping("/official/register/{plate}")
    public ResponseEntity<String> registerOfficialVehicle(@PathVariable String plate) {
        String result = parkingService.registerOfficialVehicle(plate);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

  
    @PostMapping("/resident/register/{plate}")
    public ResponseEntity<String> registerResidentVehicle(@PathVariable String plate) {
        String result = parkingService.registerResidentVehicle(plate);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

   
    @PostMapping("/start-month")
    public ResponseEntity<String> startMonth() {
        String result = parkingService.startMonth();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    
    @GetMapping("/resident/report")
    public ResponseEntity<String> generateResidentReport() {
        String report = parkingService.generateResidentReport();
        return new ResponseEntity<>(report, HttpStatus.OK);
    }
}

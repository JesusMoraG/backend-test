package com.neology.parking.repository;

import com.neology.parking.model.ResidentVehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ResidentVehicleRepository extends JpaRepository<ResidentVehicle, String> {
}

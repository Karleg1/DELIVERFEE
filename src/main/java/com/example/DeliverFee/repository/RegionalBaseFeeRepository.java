package com.example.DeliverFee.repository;

import com.example.DeliverFee.model.RegionalBaseFee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegionalBaseFeeRepository extends JpaRepository<RegionalBaseFee, Integer> {
    // Finding the base fee with specified city and vehicle type.
    @Query("SELECT rbf FROM RegionalBaseFee rbf WHERE LOWER(rbf.city) = LOWER(:city) AND LOWER(rbf.vehicleType) = LOWER(:vehicleType)")
    Optional<RegionalBaseFee> findByCityAndVehicleType(@Param("city") String city, @Param("vehicleType") String vehicleType);
}

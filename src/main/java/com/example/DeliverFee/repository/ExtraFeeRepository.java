package com.example.DeliverFee.repository;

import com.example.DeliverFee.model.ExtraFee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExtraFeeRepository extends JpaRepository<ExtraFee, Long> {
  List<ExtraFee> findByApplicableToVehicleType(String vehicleType);
}

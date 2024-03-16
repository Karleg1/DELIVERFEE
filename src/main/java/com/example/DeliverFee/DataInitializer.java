package com.example.DeliverFee;

import com.example.DeliverFee.model.ExtraFee;
import com.example.DeliverFee.model.RegionalBaseFee;
import com.example.DeliverFee.repository.ExtraFeeRepository;
import com.example.DeliverFee.repository.RegionalBaseFeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

  @Autowired
  private RegionalBaseFeeRepository feeRepository;

  
  @Autowired
  private ExtraFeeRepository extraFeeRepository;

  @Override
  public void run(String... args) throws Exception {
    // Check if the fees are already initialized to prevent duplicate entries
    if (feeRepository.count() == 0) {
      initializeFees();
      initializeExtraFees();
    }
  }

  private void initializeFees() {
    List<RegionalBaseFee> fees = Arrays.asList(
      new RegionalBaseFee("Tallinn", "Car", 4.0),
      new RegionalBaseFee("Tallinn", "Scooter", 3.5),
      new RegionalBaseFee("Tallinn", "Bike", 3.0),
      new RegionalBaseFee("Tartu", "Car", 3.5),
      new RegionalBaseFee("Tartu", "Scooter", 3.0),
      new RegionalBaseFee("Tartu", "Bike", 2.5),
      new RegionalBaseFee("Pärnu", "Car", 3.0),
      new RegionalBaseFee("Pärnu", "Scooter", 2.5),
      new RegionalBaseFee("Pärnu", "Bike", 2.0)
    );

    feeRepository.saveAll(fees);
  }

  private void initializeExtraFees() {
    if (extraFeeRepository.count() == 0) {
      List<ExtraFee> extraFees = Arrays.asList(
        new ExtraFee("TemperatureBelowMinusTen", 1.0, "Scooter"),
        new ExtraFee("TemperatureBelowMinusTen", 1.0, "Bike"),
        new ExtraFee("TemperatureBelowZero", 0.5, "Scooter"),
        new ExtraFee("TemperatureBelowZero", 0.5, "Bike"),
        new ExtraFee("WindSpeedBetween10and20", 0.5, "Bike"),
        new ExtraFee("SnowOrSleet", 1.0, "Scooter"),
        new ExtraFee("SnowOrSleet", 1.0, "Bike"),
        new ExtraFee("Rain", 0.5, "Scooter"),
        new ExtraFee("Rain", 0.5, "Bike")
      );
      extraFeeRepository.saveAll(extraFees);
    }
  }



}

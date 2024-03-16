package com.example.DeliverFee.service;

import com.example.DeliverFee.model.RegionalBaseFee;
import com.example.DeliverFee.model.WeatherData;
import com.example.DeliverFee.repository.RegionalBaseFeeRepository;
import com.example.DeliverFee.repository.WeatherDataRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;


import com.example.DeliverFee.model.ExtraFee;
import com.example.DeliverFee.repository.ExtraFeeRepository;
import java.util.Arrays;

@ExtendWith(MockitoExtension.class)
public class DeliveryFeeServiceTests {

  @Mock
  private WeatherDataRepository weatherDataRepository;

  @Mock
  private RegionalBaseFeeRepository regionalBaseFeeRepository;

  @Mock
  private ExtraFeeRepository extraFeeRepository;

  @InjectMocks
  private DeliveryFeeService deliveryFeeService;


  // Example test method for extra fees
  @Test
  void calculateDeliveryFee_WithExtraTemperatureFeeForScooter() {
    mockBaseFee("Tallinn", "Scooter", 3.5);
    mockWeatherData("Tallinn-Harku", -11.0, 3.0, "Clear");
    mockExtraFee("scooter", "TemperatureBelowMinusTen", 1.0);
    double fee = deliveryFeeService.calculateDeliveryFee("Tallinn", "Scooter");
    assertEquals(4.5, fee, "The total fee should include an extra fee for very cold temperature.");
  }

  // Test for extra fees based on wind speed for Bike
  @Test
  void calculateDeliveryFee_WindyConditionsForBike() {
    mockBaseFee("Pärnu", "Bike", 2.0);
    mockWeatherData("Pärnu", 5.0, 15.0, "Clear");
    mockExtraFee("bike", "WindSpeedBetween10and20", 0.5);
    double fee = deliveryFeeService.calculateDeliveryFee("Pärnu", "Bike");
    assertEquals(2.5, fee, "The total fee should include an extra fee for windy conditions.");
  }

  // Test for extra fees based on light snow for Scooter
  @Test
  void calculateDeliveryFee_LightSnowForScooter() {
    mockBaseFee("Tartu", "Scooter", 3.0);
    mockWeatherData("Tartu-Tõravere", 0.0, 5.0, "Light snow");
    mockExtraFee("scooter", "SnowOrSleet", 1.0);
    double fee = deliveryFeeService.calculateDeliveryFee("Tartu", "Scooter");
    assertEquals(4.0, fee, "The total fee should include an extra fee for light snow conditions.");
  }

  // Test for a forbidden weather condition (e.g., thunder) for Bike
  @Test
  void calculateDeliveryFee_ThunderForbiddenForBike() {
    mockBaseFee("Tallinn", "Bike", 3.0);
    mockWeatherData("Tallinn-Harku", 10.0, 3.0, "Thunder");
    mockExtraFee("bike", "ForbiddenPhenomenon", 0.0); // Fee is irrelevant here since it should throw an exception
    Exception exception = assertThrows(IllegalArgumentException.class, () ->
        deliveryFeeService.calculateDeliveryFee("Tallinn", "Bike")
    );
    assertEquals("Usage of vehicle type: Bike is forbidden due to severe weather conditions: Thunder", exception.getMessage());
  }

  // Test for an extra fee due to temperature slightly below zero (chilly but not extreme)
  @Test
  void calculateDeliveryFee_ChillyTemperatureForBike() {
    mockBaseFee("Tartu", "Bike", 2.5);
    mockWeatherData("Tartu-Tõravere", -5.0, 5.0, "Clear");
    mockExtraFee("bike", "TemperatureBelowZero", 0.5);
    double fee = deliveryFeeService.calculateDeliveryFee("Tartu", "Bike");
    assertEquals(3.0, fee, "The total fee should include an extra fee for chilly temperature.");
  }

  // Test for no extra fees when weather conditions are normal
  @Test
  void calculateDeliveryFee_NormalWeatherForCar() {
    mockBaseFee("Pärnu", "Car", 3.0);
    mockWeatherData("Pärnu", 15.0, 5.0, "Clear");
    // No extra fees applicable for Car in normal weather, so no need to mock extra fees
    double fee = deliveryFeeService.calculateDeliveryFee("Pärnu", "Car");
    assertEquals(3.0, fee, "The fee should only include the base fee with no extra charges.");
  }

  // Test for rain condition applying an extra fee for Scooters
  @Test
  void calculateDeliveryFee_RainForScooter() {
    mockBaseFee("Tallinn", "Scooter", 3.5);
    mockWeatherData("Tallinn-Harku", 8.0, 3.0, "Rain");
    mockExtraFee("scooter", "Rain", 0.5);
    double fee = deliveryFeeService.calculateDeliveryFee("Tallinn", "Scooter");
    assertEquals(4.0, fee, "The total fee should include an extra fee for rainy conditions.");
  }


  // Helper methods to mock the responses from repositories
  private void mockBaseFee(String city, String vehicleType, double fee) {
    when(regionalBaseFeeRepository.findByCityAndVehicleType(city.toLowerCase(), vehicleType.toLowerCase()))
          .thenReturn(Optional.of(new RegionalBaseFee(city, vehicleType, fee)));
  }

  private void mockWeatherData(String stationName, double temp, double windSpeed, String phenomenon) {
    when(weatherDataRepository.findTopByStationNameOrderByTimestampDesc(stationName))
          .thenReturn(Optional.of(new WeatherData(stationName, "123", temp, windSpeed, phenomenon, Instant.now())));
  }

  private void mockExtraFee(String vehicleType, String condition, double fee) {
    ExtraFee extraFee = new ExtraFee(condition, fee, vehicleType);
    // Use an argument matcher to ignore case
    when(extraFeeRepository.findByApplicableToVehicleType(argThat(argument -> argument.equalsIgnoreCase(vehicleType))))
          .thenReturn(Arrays.asList(extraFee));
  }
}
package com.example.DeliverFee.service;

import com.example.DeliverFee.model.ExtraFee;
import com.example.DeliverFee.model.RegionalBaseFee;
import com.example.DeliverFee.model.WeatherData;
import com.example.DeliverFee.repository.ExtraFeeRepository;
import com.example.DeliverFee.repository.RegionalBaseFeeRepository;
import com.example.DeliverFee.repository.WeatherDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Defines this class as a Spring service, responsible for calculating delivery fees based on weather conditions.
@Service
public class DeliveryFeeService {

  // Logger for this service, used for debugging and informational purposes.
  private static final Logger log = LoggerFactory.getLogger(DeliveryFeeService.class);

  // Injecting the repository for accessing weather data.
  @Autowired
  private WeatherDataRepository weatherDataRepository;

  // Injecting the repository for accessing regional base fee data.
  @Autowired
  private RegionalBaseFeeRepository regionalBaseFeeRepository;

  @Autowired
  private ExtraFeeRepository extraFeeRepository;

  private static final Map<String, String> CITY_TO_STATION_MAP = new HashMap<>();
  static {
    CITY_TO_STATION_MAP.put("tallinn", "Tallinn-Harku");
    CITY_TO_STATION_MAP.put("tartu", "Tartu-Tõravere");
    CITY_TO_STATION_MAP.put("pärnu", "Pärnu");
  }

  public double calculateDeliveryFee(String city, String vehicleType) {
    String normalizedCity = city.trim().toLowerCase();
     String normalizedVehicleType = vehicleType.trim().toLowerCase();

    // Retrieve the regional base fee for the given city and vehicle type from the database
    Optional<RegionalBaseFee> baseFeeObj = regionalBaseFeeRepository.findByCityAndVehicleType(normalizedCity, normalizedVehicleType);
    if (!baseFeeObj.isPresent()) {
      throw new IllegalArgumentException("No base fee found for city: " + city + " and vehicle type: " + vehicleType);
    }
    double baseFee = baseFeeObj.get().getFee();

    // Map the normalized city to the station name for weather data retrieval
    String stationName = CITY_TO_STATION_MAP.get(normalizedCity);
    if (stationName == null) {
      throw new IllegalArgumentException("No station mapped for city: " + city);
    }

    Optional<WeatherData> latestWeatherData = weatherDataRepository.findTopByStationNameOrderByTimestampDesc(stationName);
    if (!latestWeatherData.isPresent()) {
      log.warn("No weather data available for station: {}. Returning default fee: {} €", stationName, baseFee);
      return baseFee;
    }

    WeatherData weatherData = latestWeatherData.get();
    double fee = baseFee + calculateExtraFees(weatherData, vehicleType);

    log.info("Calculated total delivery fee for city: {}, vehicle type: {} is: {} €", city, vehicleType, fee);
    return fee;
  }

  private double calculateExtraFees(WeatherData weatherData, String vehicleType) {
    double extraFee = 0.0;
    List<ExtraFee> extraFees = extraFeeRepository.findByApplicableToVehicleType(vehicleType);

    for (ExtraFee fee : extraFees) {
      switch (fee.getCondition()) {
        case "TemperatureBelowMinusTen":
          if (weatherData.getAirTemperature() < -10) extraFee += fee.getFee();
          break;
        case "TemperatureBelowZero":
          if (weatherData.getAirTemperature() < 0 && weatherData.getAirTemperature() >= -10) extraFee += fee.getFee();
          break;
        case "WindSpeedOver20":
          if (weatherData.getWindSpeed() >= 20) throw new IllegalArgumentException("Usage of vehicle type: " + vehicleType + " is forbidden due to high wind speed");
          break;
        case "WindSpeedBetween10and20":
          if (weatherData.getWindSpeed() >= 10 && weatherData.getWindSpeed() < 20) extraFee += fee.getFee();
          break;
        case "SnowOrSleet":
          if (isSnowOrSleet(weatherData.getPhenomenon())) extraFee += fee.getFee();
          break;
        case "Rain":
          if (isRain(weatherData.getPhenomenon())) extraFee += fee.getFee();
          break;
        case "ForbiddenPhenomenon":
          if (isForbiddenPhenomenon(weatherData.getPhenomenon())) throw new IllegalArgumentException("Usage of vehicle type: " + vehicleType + " is forbidden due to severe weather conditions: " + weatherData.getPhenomenon());
          break;
      }
    }
    return extraFee;
  }


  // Helper methods to determine the impact of weather phenomena on delivery fees.
  // Either higher fee or forbid vehicle type.
  private boolean isSnowOrSleet(String phenomenon) {
    String lowerCasePhenomenon = phenomenon.toLowerCase();
    return lowerCasePhenomenon.contains("snow") || lowerCasePhenomenon.contains("sleet");
  }

  private boolean isRain(String phenomenon) {
    String lowerCasePhenomenon = phenomenon.toLowerCase();
    return lowerCasePhenomenon.contains("shower") || lowerCasePhenomenon.contains("rain");
  }

  private boolean isForbiddenPhenomenon(String phenomenon) {
    String lowerCasePhenomenon = phenomenon.toLowerCase();
    return lowerCasePhenomenon.contains("glaze") || lowerCasePhenomenon.contains("hail") || lowerCasePhenomenon.contains("thunder");
  }

}

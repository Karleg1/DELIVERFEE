package com.example.DeliverFee.repository;

import com.example.DeliverFee.model.WeatherData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

// Interface to manage CRUD operations for WeatherData entities, leveraging Spring Data JPA.
@Repository
public interface WeatherDataRepository extends JpaRepository<WeatherData, Integer> {
  // Finding the most recent weather data entry for a given station name.
  Optional<WeatherData> findTopByStationNameOrderByTimestampDesc(String stationName);
}

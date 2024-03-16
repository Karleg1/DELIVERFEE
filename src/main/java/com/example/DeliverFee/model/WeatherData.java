package com.example.DeliverFee.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.Instant;

// Represents the weather data collected from weather stations, mapped as a JPA entity.
@Entity
public class WeatherData {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  private String stationName;
  private String wmocode;
  private Double airTemperature;
  private Double windSpeed;
  private String phenomenon;
  private Instant timestamp;

  public WeatherData() {
  }

  public WeatherData(String stationName, String wmocode, Double airTemperature, Double windSpeed, String phenomenon, Instant timestamp) {
    this.stationName = stationName;
    this.wmocode = wmocode;
    this.airTemperature = airTemperature;
    this.windSpeed = windSpeed;
    this.phenomenon = phenomenon;
    this.timestamp = timestamp;
  }

  // getters and setters.
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getStationName() {
    return stationName;
  }

  public void setStationName(String stationName) {
    this.stationName = stationName;
  }

  public String getWmocode() {
    return wmocode;
  }

  public void setWmocode(String wmocode) {
    this.wmocode = wmocode;
  }

  public Double getAirTemperature() {
    return airTemperature;
  }

  public void setAirTemperature(Double airTemperature) {
    this.airTemperature = airTemperature;
  }

  public Double getWindSpeed() {
    return windSpeed;
  }

  public void setWindSpeed(Double windSpeed) {
    this.windSpeed = windSpeed;
  }

  public String getPhenomenon() {
    return phenomenon;
  }

  public void setPhenomenon(String phenomenon) {
    this.phenomenon = phenomenon;
  }

  public Instant getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Instant timestamp) {
    this.timestamp = timestamp;
  }
}

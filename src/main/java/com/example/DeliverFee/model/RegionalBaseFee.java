package com.example.DeliverFee.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class RegionalBaseFee {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;
  
  private String city;
  private String vehicleType;
  private Double fee;
  
  public RegionalBaseFee() {}
  
  public RegionalBaseFee(String city, String vehicleType, Double fee) {
    this.city = city;
    this.vehicleType = vehicleType;
    this.fee = fee;
  }

  // Getters
  public Integer getId() {
    return id;
  }

  public String getCity() {
    return city;
  }

  public String getVehicleType() {
    return vehicleType;
  }

  public Double getFee() {
    return fee;
  }

  // Setters
  public void setId(Integer id) {
    this.id = id;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public void setVehicleType(String vehicleType) {
    this.vehicleType = vehicleType;
  }

  public void setFee(Double fee) {
    this.fee = fee;
  }
}

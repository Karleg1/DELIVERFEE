package com.example.DeliverFee.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ExtraFee {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  
  private String condition;
  private Double fee;
  private String applicableToVehicleType;

  // Constructors
  public ExtraFee() {}

  public ExtraFee(String condition, Double fee, String applicableToVehicleType) {
      this.condition = condition;
      this.applicableToVehicleType = applicableToVehicleType;
      this.fee = fee;
  }

  // Getters and setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCondition() {
    return condition;
  }

  public void setCondition(String condition) {
    this.condition = condition;
  }

  public Double getFee() {
    return fee;
  }

  public void setFee(Double fee) {
    this.fee = fee;
  }

  public String getApplicableToVehicleType() {
    return applicableToVehicleType;
  }

  public void setApplicableToVehicleType(String applicableToVehicleType) {
    this.applicableToVehicleType = applicableToVehicleType;
  }

}

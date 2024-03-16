package com.example.DeliverFee.controller;

import com.example.DeliverFee.model.ExtraFee;
import com.example.DeliverFee.model.RegionalBaseFee;
import com.example.DeliverFee.repository.ExtraFeeRepository;
import com.example.DeliverFee.repository.RegionalBaseFeeRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

// The controller for handling API requests related to regional base fees.
@RestController
@RequestMapping("/api/fees")
public class FeesController {

  // Inject the repository to interact with the database for fee operations.
  @Autowired
  private RegionalBaseFeeRepository regionalBaseFeeRepository;

  @Autowired
  private ExtraFeeRepository extraFeeRepository;

  // Endpoint to either create a new fee or update an existing one based on city and vehicle type.
  @Operation(summary = "Create or update a regional base fee", description = "Creates a new regional base fee or updates an existing one based on city and vehicle type.")
  @ApiResponse(responseCode = "200", description = "Successfully created or updated the fee")
  @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)

  @PostMapping("/regionalBaseFee")
  public ResponseEntity<RegionalBaseFee> createOrUpdateRegionalBaseFee(@RequestBody RegionalBaseFee regionalBaseFee) {
    Optional<RegionalBaseFee> existingFee = regionalBaseFeeRepository.findByCityAndVehicleType(regionalBaseFee.getCity(), regionalBaseFee.getVehicleType());
    if (existingFee.isPresent()) {
      RegionalBaseFee updatedFee = existingFee.get();
      updatedFee.setFee(regionalBaseFee.getFee());
      regionalBaseFeeRepository.save(updatedFee);
      return ResponseEntity.ok(updatedFee);
    } else {
      RegionalBaseFee newFee = regionalBaseFeeRepository.save(regionalBaseFee);
      return ResponseEntity.ok(newFee);
    }
  }

  // Retrieves all fee entries in the system.
  @Operation(summary = "Get all regional base fees",  description = "Retrieves a list of all regional base fees.")
  @ApiResponse(responseCode = "200", description = "Successfully retrieved all fees")
  @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)

  @GetMapping("/regionalBaseFee")
  public ResponseEntity<List<RegionalBaseFee>> getAllFees() {
    return ResponseEntity.ok(regionalBaseFeeRepository.findAll());
  }

  // Allows for retrieval of a specific fee by city and vehicle type.
  @Operation(summary = "Get regional base fee by city and vehicle type", description = "Retrieves a specific regional base fee based on the given city and vehicle type.")
  @ApiResponse(responseCode = "200", description = "Successfully retrieved the fee")
  @ApiResponse(responseCode = "404", description = "Fee not found for the given city and vehicle type")
  @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)

  @GetMapping("/regionalBaseFee/{city}/{vehicleType}")
  public ResponseEntity<RegionalBaseFee> getFeeByCityAndVehicleType(
    @Parameter(description = "The name of the city to retrieve the fee for") @PathVariable String city,
    @Parameter(description = "The type of vehicle to retrieve the fee for") @PathVariable String vehicleType) {
    Optional<RegionalBaseFee> fee = regionalBaseFeeRepository.findByCityAndVehicleType(city, vehicleType);
    return fee.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  // Endpoint to remove a fee from the system.
  @Operation(summary = "Delete regional base fee by city and vehicle type", description = "Deletes a specific regional base fee based on the given city and vehicle type.")
  @ApiResponse(responseCode = "200", description = "Successfully deleted the fee")
  @ApiResponse(responseCode = "404", description = "Fee not found for the given city and vehicle type")
  @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content) 
  
  @DeleteMapping("/regionalBaseFee/{city}/{vehicleType}")
  public ResponseEntity<?> deleteRegionalBaseFee(
    @Parameter(description = "The name of the city for which the fee should be deleted") @PathVariable String city,
    @Parameter(description = "The type of vehicle for which the fee should be deleted") @PathVariable String vehicleType) {
    Optional<RegionalBaseFee> fee = regionalBaseFeeRepository.findByCityAndVehicleType(city, vehicleType);
    if (fee.isPresent()) {
      regionalBaseFeeRepository.delete(fee.get());
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }



  
 
  @Operation(summary = "Create a new extra fee", description = "Adds a new extra fee rule to the database.")
  @ApiResponse(responseCode = "200", description = "Successfully created the extra fee")
  @PostMapping("/extraFee")
  public ResponseEntity<ExtraFee> createExtraFee(@RequestBody ExtraFee extraFee) {
    ExtraFee savedExtraFee = extraFeeRepository.save(extraFee);
    return ResponseEntity.ok(savedExtraFee);
  }

  @Operation(summary = "Retrieve all extra fees", description = "Gets a list of all extra fees.")
  @ApiResponse(responseCode = "200", description = "Successfully retrieved all extra fees")
  @GetMapping("/extraFee")
  public ResponseEntity<List<ExtraFee>> getAllExtraFees() {
    List<ExtraFee> fees = extraFeeRepository.findAll();
    return ResponseEntity.ok(fees);
  }

  @Operation(summary = "Retrieve an extra fee by ID", description = "Gets a specific extra fee by its ID.")
  @ApiResponse(responseCode = "200", description = "Successfully retrieved the extra fee")
  @ApiResponse(responseCode = "404", description = "Extra fee not found")
  @GetMapping("/extraFee/{id}")
  public ResponseEntity<ExtraFee> getExtraFeeById(@PathVariable Long id) {
    Optional<ExtraFee> extraFee = extraFeeRepository.findById(id);
    return extraFee.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @Operation(summary = "Update an extra fee", description = "Updates a specific extra fee by its ID.")
  @ApiResponse(responseCode = "200", description = "Successfully updated the extra fee")
  @ApiResponse(responseCode = "404", description = "Extra fee not found")
  @PutMapping("/extraFee/{id}")
  public ResponseEntity<ExtraFee> updateExtraFee(@PathVariable Long id, @RequestBody ExtraFee extraFeeDetails) {
    Optional<ExtraFee> extraFeeOptional = extraFeeRepository.findById(id);
    if (extraFeeOptional.isPresent()) {
      ExtraFee updatedFee = extraFeeOptional.get();
      updatedFee.setCondition(extraFeeDetails.getCondition());
      updatedFee.setFee(extraFeeDetails.getFee());
      updatedFee.setApplicableToVehicleType(extraFeeDetails.getApplicableToVehicleType());
      extraFeeRepository.save(updatedFee);
      return ResponseEntity.ok(updatedFee);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @Operation(summary = "Delete an extra fee", description = "Removes a specific extra fee by its ID from the database.")
  @ApiResponse(responseCode = "200", description = "Successfully deleted the extra fee")
  @ApiResponse(responseCode = "404", description = "Extra fee not found")
  @DeleteMapping("/extraFee/{id}")
  public ResponseEntity<?> deleteExtraFee(@PathVariable Long id) {
    if (extraFeeRepository.existsById(id)) {
      extraFeeRepository.deleteById(id);
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }
}

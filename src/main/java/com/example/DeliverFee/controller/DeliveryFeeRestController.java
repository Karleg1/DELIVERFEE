package com.example.DeliverFee.controller;

import com.example.DeliverFee.service.DeliveryFeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

// REST controller for the 'calculateDeliveryFee' endpoint.
@RestController
public class DeliveryFeeRestController {

  
  // Injected the service layer that contains business logic.
  @Autowired
  private DeliveryFeeService deliveryFeeService;

  @Operation(summary = "Calculate delivery fee",
    description = "Calculates the delivery fee based on the provided city and vehicle type.",
    responses = {
    @ApiResponse(responseCode = "200", description = "Successfully calculated fee"),
    @ApiResponse(responseCode = "400", description = "Error based on the input parameters", content = @Content),
    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
  })
  // This endpoint is triggered with a GET request, accepting 'city' and 'vehicleType' as request parameters.
  @GetMapping("/api/calculateDeliveryFee")
  public ResponseEntity<?> calculateDeliveryFee(
      @Parameter(description = "Name of the city", required = true) @RequestParam String city,
      @Parameter(description = "Type of the vehicle", required = true) @RequestParam String vehicleType) {
    try {
      // Uses the delivery fee service to calculate the fee and wraps the response in an OK status.
      double fee = deliveryFeeService.calculateDeliveryFee(city, vehicleType);
      return ResponseEntity.ok(Map.of("totalFee", fee));
    } catch (IllegalArgumentException e) {
      // Catches and handles bad requests, such as when parameters do not meet business rules, by returning a client error response.
      return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    } catch (Exception e) {
      // Handles any unexpected errors during the fee calculation process with a generic error response.
      return ResponseEntity.internalServerError().body(Map.of("error", "An error occurred while processing your request."));
    }
  }
  
}

package com.example.DeliverFee.controller;

import com.example.DeliverFee.service.DeliveryFeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

// Handles web requests for calculating delivery fees and displaying them on a web page.
@Controller
public class DeliveryFeeController {

	@Autowired
	private DeliveryFeeService deliveryFeeService;

	// Displays the fee calculator form.
	@GetMapping("/")
	public String showCalculatorForm() {
		return "index";
	}

	// Processes the form submission and calculates the fee.
	@PostMapping("/calculate")
	public String calculateFee(@RequestParam String city, @RequestParam String vehicleType, Model model) {
		try {
			double fee = deliveryFeeService.calculateDeliveryFee(city, vehicleType);
			model.addAttribute("fee", fee);
		} catch (IllegalArgumentException e) {
			model.addAttribute("errorMessage", e.getMessage());
		}
		return "index";
	}
}

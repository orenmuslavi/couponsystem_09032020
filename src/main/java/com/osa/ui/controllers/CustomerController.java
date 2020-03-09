package com.osa.ui.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.osa.security.services.UserDetailsImpl;
import com.osa.service.CustomerService;
import com.osa.ui.model.request.CouponRequestModel;

@CrossOrigin("*")
@RestController
@PreAuthorize("hasRole('CUSTOMER')") // Security: Authorize end points only if currently logged-in user is a Customer
@RequestMapping(path = "/customer")
public class CustomerController {
	
	@Autowired 
	CustomerService customerService;
	
	// POST - /customer/coupon
	@PostMapping(path = "/coupon", consumes = MediaType.APPLICATION_JSON_VALUE, 
								   produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> purchaseCoupon(@RequestBody CouponRequestModel coupon) {
		return new ResponseEntity<>(customerService.purchaseCoupon(getCurrentUser(), coupon), HttpStatus.OK);
	}
	
	// GET - /customer/coupons
	@GetMapping(path = "/coupons", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCustomerCoupons() {
		return new ResponseEntity<>(customerService.getCustomerCoupons(getCurrentUser()), HttpStatus.OK);
	}
	
	// GET - /customer/coupons/{value}
	@GetMapping(path = "/coupons/{value}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCustomerCoupons(@PathVariable String value) {
		return new ResponseEntity<>(customerService.getCustomerCoupons(getCurrentUser(), value), HttpStatus.OK);
	}
	
	@GetMapping(path = "/details", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCustomerDetails() {
		return new ResponseEntity<>(customerService.getCustomerDetails(getCurrentUser()), HttpStatus.OK);
	}
	
	// extract currently logged in user name from SecurityContextHolder 
	private String getCurrentUser() {
		return ((UserDetailsImpl) SecurityContextHolder
				.getContext()
				.getAuthentication()
				.getPrincipal())
				.getEmail();
	}
	
}

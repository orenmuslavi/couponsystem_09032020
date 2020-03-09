package com.osa.ui.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.osa.security.services.UserDetailsImpl;
import com.osa.service.CompanyService;
import com.osa.ui.model.request.CouponRequestModel;
import com.osa.ui.model.response.CouponRest;

@CrossOrigin("*")
@RestController
@PreAuthorize("hasRole('COMPANY')") // // Security: Authorize end points only if currently logged-in user is a Company
@RequestMapping(path = "/company")
public class CompanyController {
	
	@Autowired
	CompanyService companyService;
	
	/***************************** Coupon API *****************************/
	
	// POST - /company/coupon
	@PostMapping(path = "/coupon", 
			consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> addCoupon(@RequestBody CouponRequestModel coupon) {
		return new ResponseEntity<>(companyService.addCoupon(getCurrentUser(), coupon), HttpStatus.OK);
	}

	// PUT - /company/coupon/{couponId}
	@PutMapping(path = "/coupon/{couponId}",
			consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateCoupon(@PathVariable String couponId, @RequestBody CouponRequestModel coupon) {
		return new ResponseEntity<>(companyService.updateCoupon(getCurrentUser(), couponId, coupon), HttpStatus.OK);
	}
	
	// DELETE - /company/coupon/{couponId}
	@DeleteMapping(path = "/coupon/{couponId}")
	public ResponseEntity<?> deleteCoupon(@PathVariable String couponId) {
		companyService.deleteCoupon(getCurrentUser(), couponId);
		return new ResponseEntity<>("Success", HttpStatus.OK);
	}
	
	// GET - /company/coupons
	@GetMapping(path = "/coupons", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAllCoupons() {
		return new ResponseEntity<>(companyService.getCompanyCoupons(getCurrentUser()), HttpStatus.OK);
	}
	
	// GET - /company/coupons
	@GetMapping(path = "/coupon/{couponId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCouponById(@PathVariable String couponId) {
		CouponRest couponRest = companyService.getCouponById(couponId);
		return new ResponseEntity<>(couponRest, HttpStatus.OK);
	}
	
	// GET - /company/coupons/{value}
	@GetMapping(path = "/coupons/{value}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCouponsByCategory(@PathVariable String value) {
		return new ResponseEntity<>(companyService.getCompanyCoupons(getCurrentUser(), value), HttpStatus.OK);
	}
	
	@GetMapping(path = "/details", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCompanyDetails() {		
		return new ResponseEntity<>(companyService.getCompanyDetails(getCurrentUser()), HttpStatus.OK);
	}
	
	// extract currently logged in username(email) from SecurityContextHolder 
	private String getCurrentUser() {
		return ((UserDetailsImpl) SecurityContextHolder
				.getContext()
				.getAuthentication()
				.getPrincipal())
				.getEmail();
	}
	
}

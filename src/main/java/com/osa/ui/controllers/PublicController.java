package com.osa.ui.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.osa.security.jwt.JwtProvider;
import com.osa.security.services.UserDetailsImpl;
import com.osa.service.AdministratorService;
import com.osa.ui.model.request.CompanyRequestModel;
import com.osa.ui.model.request.CustomerRequestModel;
import com.osa.ui.model.request.UserLoginRequestModel;
import com.osa.ui.model.response.CustomerRest;
import com.osa.ui.model.response.JwtResponse;

@CrossOrigin("*")
@RestController
@RequestMapping(path = "/")
public class PublicController {
	
	@Autowired
	AdministratorService administratorService;
	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired
	JwtProvider jwtProvider;

	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody UserLoginRequestModel loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwt = jwtProvider.generateJwtToken(authentication);
		UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();
		String userName = (userDetailsImpl.getName() + " " + (userDetailsImpl.getLastName())).trim();
		
		return ResponseEntity.ok(
				new JwtResponse(
				jwt, 
				userName, 
				userDetailsImpl.getEmail(), 
				userDetailsImpl.getAuthorities()));
	}

	@PostMapping(path = "/register/company")
	public ResponseEntity<?> companySignup(@RequestBody CompanyRequestModel details) {
		return new ResponseEntity<>(administratorService.addCompany(details), HttpStatus.OK);
	}
	
	@PostMapping(path = "/register/customer")
	public ResponseEntity<?> customerSignup(@RequestBody CustomerRequestModel details) {
		return new ResponseEntity<>(administratorService.addCustomer(details), HttpStatus.OK);
	}
	
	@GetMapping		// get all coupons to display on home page
	public ResponseEntity<?> getAllCoupons() {
		return new ResponseEntity<>(administratorService.getAllCoupons(), HttpStatus.OK);
	}	
	
	
	
}

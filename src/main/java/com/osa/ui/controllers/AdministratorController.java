package com.osa.ui.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.osa.service.AdministratorService;
import com.osa.ui.model.request.CompanyRequestModel;
import com.osa.ui.model.request.CustomerRequestModel;

@CrossOrigin("*")
@RestController
@PreAuthorize("hasRole('ADMIN')")	// Security: end points are accessible only by Admin
@RequestMapping(path = "/admin")
public class AdministratorController {

	@Autowired
	AdministratorService administratorService;

	/***************************** Company end points *****************************/
	
	// POST - /admin/company
	@PostMapping(path = "/company", 
			consumes = { MediaType.APPLICATION_JSON_VALUE }, 
			produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> addCompany(@RequestBody CompanyRequestModel company) {
		return new ResponseEntity<>(administratorService.addCompany(company), HttpStatus.OK);
	}

	//PUT - /admin/company/{companyId}
	@PutMapping(path = "/company/{companyId}", 
			consumes = { MediaType.APPLICATION_JSON_VALUE }, 
			produces = {MediaType.APPLICATION_JSON_VALUE }) 
	public ResponseEntity<?> updateCompany(@PathVariable("companyId") String companyId, @RequestBody CompanyRequestModel company) {
		return new ResponseEntity<>(administratorService.updateCompany(companyId, company), HttpStatus.OK);
	}
	
	// DELETE - /admin/company/{companyId}
	@DeleteMapping(path = "/company/{companyId}")
	public ResponseEntity<?> deleteCompany(@PathVariable("companyId") String companyId) {
		return new ResponseEntity<>(administratorService.deleteCompany(companyId), HttpStatus.OK);
	}
	
	// GET - /admin/company/{companyId}
	@GetMapping(path = "/company/{companyId}", 
			produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> getCompanyById(@PathVariable("companyId") String companyId) {
		return new ResponseEntity<>(administratorService.getCompanyById(companyId), HttpStatus.OK);
	}
	
	// GET - /admin/companies/
	@GetMapping(path = "/companies", 
			produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> getAllCompanies() {
		return new ResponseEntity<>(administratorService.getAllCompanies(), HttpStatus.OK);
	}
	
	/*********************************************** Customer end points ***********************************************/
	
	// POST - /admin/customer
	@PostMapping(path = "/customer", 
			consumes = { MediaType.APPLICATION_JSON_VALUE }, 
			produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> addCustomer(@RequestBody CustomerRequestModel customer) {
		return new ResponseEntity<>(administratorService.addCustomer(customer), HttpStatus.OK);
	}

	//PUT - /admin/customer/{customerId}
	@PutMapping(path = "/customer/{customerId}", 
			consumes = { MediaType.APPLICATION_JSON_VALUE }, 
			produces = {MediaType.APPLICATION_JSON_VALUE }) 
	public ResponseEntity<?> updateCustomer(
			@PathVariable("customerId") String customerId, @RequestBody CustomerRequestModel customer) {
		return new ResponseEntity<>(administratorService.updateCustomer(customerId, customer), HttpStatus.OK);
	}
	
	// DELETE - /admin/customer/{customerId}
	@DeleteMapping(path = "/customer/{customerId}", 
			produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> deleteCustomer(@PathVariable("customerId") String customerId) {
		return new ResponseEntity<>(administratorService.deleteCustomer(customerId), HttpStatus.OK);
	}
	
	// GET - /admin/customer/{customerId}
	@GetMapping(path = "/customer/{customerId}", 
			produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> getCustomerById(@PathVariable("customerId") String customerId) {
		return new ResponseEntity<>(administratorService.getCustomerById(customerId), HttpStatus.OK);
	}
	
	// GET - /admin/customers/
	@GetMapping(path = "/customers", 
			produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> getAllCustomers() {
		return new ResponseEntity<>(administratorService.getAllCustomers(), HttpStatus.OK);
	}
	
//	@GetMapping(path = "/details")
//	public ResponseEntity<?> getAdminBoard() {
//		return new ResponseEntity<>(">> Admin Board", HttpStatus.OK);
//	}
	
}

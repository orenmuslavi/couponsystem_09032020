package com.osa.service.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.osa.io.entity.CompanyEntity;
import com.osa.io.entity.CustomerEntity;
import com.osa.io.repositories.CompanyRepository;
import com.osa.io.repositories.CouponRepository;
import com.osa.io.repositories.CustomerRepository;
import com.osa.service.AdministratorService;
import com.osa.shared.Utils;
import com.osa.ui.exceptions.ApplicationException;
import com.osa.ui.model.request.CompanyRequestModel;
import com.osa.ui.model.request.CustomerRequestModel;
import com.osa.ui.model.response.CompanyRest;
import com.osa.ui.model.response.CouponRest;
import com.osa.ui.model.response.CustomerRest;
import com.osa.ui.model.response.ErrorMessages;

@Service
public class AdministratorServiceImpl implements AdministratorService {

	@Autowired
	CompanyRepository companyRepository;
	@Autowired
	CustomerRepository customerRepository;
	@Autowired
	CouponRepository couponRepository;
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	Utils utils;
	final int idLength = 20; // string length of generated public id
	@Autowired
	ModelMapper modelMapper;
	
	/*********************************************** Company CRUD ***********************************************/

	@Override
	public CompanyRest addCompany(CompanyRequestModel company) {
		
		if (companyRepository.findByName(company.getName()) != null) {
			throw new ApplicationException(ErrorMessages.RECORD_SAME_NAME_EXISTS.getErrorMessage());
		}

		// Email's are unique for both customer and company
		if (isRecordExistsByEmail(company.getEmail())) {
			throw new ApplicationException(ErrorMessages.RECORD_SAME_EMAIL_EXISTS.getErrorMessage());
		}

		// check for valid email
		if ( !utils.isEmailValid(company.getEmail()) ) {
			throw new ApplicationException(ErrorMessages.INVALID_EMAIL.getErrorMessage());
		}
		
		// FIXME: check password length
		
		CompanyEntity companyEntity = modelMapper.map(company, CompanyEntity.class);
		companyEntity.setCompanyId(utils.generateId(idLength));
		companyEntity.setPassword(bCryptPasswordEncoder.encode(company.getPassword()));

		CompanyRest returnValue = modelMapper.map(companyRepository.save(companyEntity), CompanyRest.class);

		return returnValue;
	}
	
	@Override
	public CompanyRest updateCompany(String companyId, CompanyRequestModel company) {
		CompanyEntity companyEntity = companyRepository.findByCompanyId(companyId);
		
		if (companyEntity == null) {
			throw new ApplicationException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		}

		if ( !companyEntity.getEmail().equals(company.getEmail()) ) {
			// company want to change its email
			if (isRecordExistsByEmail(company.getEmail())) {
				// there is a record with the same email
				throw new ApplicationException(ErrorMessages.RECORD_SAME_EMAIL_EXISTS.getErrorMessage());
			}
			
			if ( !utils.isEmailValid(company.getEmail()) ) {
				// email is in-valid
				throw new ApplicationException(ErrorMessages.INVALID_EMAIL.getErrorMessage());
			}
			
			companyEntity.setEmail(company.getEmail());
		}
		
		if ( !bCryptPasswordEncoder.matches(company.getPassword(), companyEntity.getPassword()) ) {
			// password has been changed, update
			companyEntity.setPassword(bCryptPasswordEncoder.encode(company.getPassword()));
		}

		CompanyRest returnValue = modelMapper.map(companyRepository.save(companyEntity), CompanyRest.class);
		return returnValue;
	}

	@Override
	public CompanyRest deleteCompany(String companyId) {
		CompanyEntity companyEntity = companyRepository.findByCompanyId(companyId);
		
		if (companyEntity == null) {
			throw new ApplicationException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		}
		
		CompanyRest deletedCompany = modelMapper.map(companyEntity, CompanyRest.class);
		companyRepository.delete(companyEntity);
		return deletedCompany;
	}
	
	@Override
	public CompanyRest getCompanyById(String companyId) {
		CompanyEntity companyEntity = companyRepository.findByCompanyId(companyId);
		
		if (companyEntity == null) {
			throw new ApplicationException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		}
		
		CompanyRest returnValue = modelMapper.map(companyEntity, CompanyRest.class);
		
		// copy all companyEntity coupons to returnValue
		Type listType = new TypeToken<List<CouponRest>>() {}.getType();
		returnValue.setCoupons(modelMapper.map(companyEntity.getCoupons(), listType));
		
		return returnValue;
	}

	@Override
	public List<CompanyRest> getAllCompanies() {
		List<CompanyEntity> allCompanies = companyRepository.findAll();
		List<CompanyRest> returnValue = new ArrayList<>();
		
		for (CompanyEntity companyEntity : allCompanies) {
			returnValue.add(modelMapper.map(companyEntity, CompanyRest.class));
		}
		
		return returnValue;
	}
	
	/*********************************************** Customer CRUD ***********************************************/
	
	@Override
	public CustomerRest addCustomer(CustomerRequestModel customer) {
		
		if (isRecordExistsByEmail(customer.getEmail())) {
			throw new ApplicationException(ErrorMessages.RECORD_SAME_EMAIL_EXISTS.getErrorMessage());
		}
		
		if ( !utils.isEmailValid(customer.getEmail()) ) {
			throw new ApplicationException(ErrorMessages.INVALID_EMAIL.getErrorMessage());
		}
		
		CustomerEntity customerEntity = modelMapper.map(customer, CustomerEntity.class);
		String publicId = utils.generateId(idLength);
		customerEntity.setCustomerId(publicId);
		customerEntity.setPassword(bCryptPasswordEncoder.encode(customer.getPassword()));
		
		return modelMapper.map(customerRepository.save(customerEntity), CustomerRest.class);
	}

	@Override
	public CustomerRest updateCustomer(String customerId, CustomerRequestModel customer) {
		CustomerEntity customerEntity = customerRepository.findByCustomerId(customerId);
		
		if (customerEntity == null) {
			throw new ApplicationException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		}
		
		if ( !customerEntity.getFirstName().equals(customer.getFirstName()) ) {
			// change first name
			customerEntity.setFirstName(customer.getFirstName());
		}
		
		if ( !customerEntity.getLastName().equals(customer.getLastName()) ) {
			// change last name
			customerEntity.setLastName(customer.getLastName());
		}
		
		if ( !customerEntity.getEmail().equals(customer.getEmail()) ) {
			// customer wants to change its email
			if (isRecordExistsByEmail(customer.getEmail())) {
				// there is already an email like this
				throw new ApplicationException(ErrorMessages.RECORD_SAME_EMAIL_EXISTS.getErrorMessage());
			}
			
			if ( !utils.isEmailValid(customer.getEmail()) ) {
				// email is not valid
				throw new ApplicationException(ErrorMessages.INVALID_EMAIL.getErrorMessage());
			}
			// change email
			customerEntity.setEmail(customer.getEmail());
		}
		
		if ( !bCryptPasswordEncoder.matches(customer.getPassword(), customerEntity.getPassword()) ) {
			// change password
			customerEntity.setPassword(bCryptPasswordEncoder.encode(customer.getPassword()));
		}

		return modelMapper.map(customerRepository.save(customerEntity), CustomerRest.class);
	}

	@Override
	public CustomerRest deleteCustomer(String customerId) {
		CustomerEntity customerEntity = customerRepository.findByCustomerId(customerId);
		
		if (customerEntity == null) {
			throw new ApplicationException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		}
		
		CustomerRest deletedCustomer = modelMapper.map(customerEntity, CustomerRest.class);
		customerRepository.delete(customerEntity);
		
		return deletedCustomer;
	}

	@Override
	public CustomerRest getCustomerById(String customerId) {
		CustomerEntity customerEntity = customerRepository.findByCustomerId(customerId);
		
		if (customerEntity == null) {
			throw new ApplicationException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		}
		
		CustomerRest returnValue = modelMapper.map(customerEntity, CustomerRest.class);
		Type listType = new TypeToken<List<CouponRest>>() {}.getType();
		returnValue.setCoupons(modelMapper.map(returnValue.getCoupons(), listType));
		
		return returnValue;
	}
	
	@Override
	public List<CustomerRest> getAllCustomers() {
		List<CustomerEntity> allCustomers = customerRepository.findAll();
		List<CustomerRest> returnValue = new ArrayList<>();
		
		for (CustomerEntity customerEntity : allCustomers) {
			returnValue.add(modelMapper.map(customerEntity, CustomerRest.class));
		}
		
		return returnValue;
	}
	
	@Override
	public List<CouponRest> getAllCoupons() {
		Type listType = new TypeToken<List<CouponRest>>() {}.getType();
		return modelMapper.map(couponRepository.findAll(), listType);
	}

	private boolean isRecordExistsByEmail(String email) {
		return companyRepository.findByEmail(email) != null || 
				customerRepository.findByEmail(email) != null ||
				"admin@admin.com".equalsIgnoreCase(email); // Don't allow users to register with admin email
	}
	
}

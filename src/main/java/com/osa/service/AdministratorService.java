package com.osa.service;

import java.util.List;

import com.osa.ui.model.request.CompanyRequestModel;
import com.osa.ui.model.request.CustomerRequestModel;
import com.osa.ui.model.response.CompanyRest;
import com.osa.ui.model.response.CouponRest;
import com.osa.ui.model.response.CustomerRest;

//public interface AdministratorService extends UserDetailsService {
	public interface AdministratorService {

	CompanyRest addCompany(CompanyRequestModel company);
	CompanyRest updateCompany(String companyId, CompanyRequestModel company);
	CompanyRest deleteCompany(String companyId);
	CompanyRest getCompanyById(String companyId);
	List<CompanyRest> getAllCompanies();

	CustomerRest addCustomer(CustomerRequestModel customer);
	CustomerRest updateCustomer(String customerId, CustomerRequestModel customer);
	CustomerRest deleteCustomer(String customerId);
	CustomerRest getCustomerById(String customerId);
	List<CustomerRest> getAllCustomers();
	
	List<CouponRest> getAllCoupons();
	
}

package com.osa.service.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.osa.io.entity.CompanyEntity;
import com.osa.io.entity.CouponEntity;
import com.osa.io.entity.CustomerEntity;
import com.osa.io.repositories.CompanyRepository;
import com.osa.io.repositories.CouponRepository;
import com.osa.io.repositories.CustomerRepository;
import com.osa.service.CustomerService;
import com.osa.ui.exceptions.ApplicationException;
import com.osa.ui.model.request.CouponRequestModel;
import com.osa.ui.model.response.CouponRest;
import com.osa.ui.model.response.CustomerRest;
import com.osa.ui.model.response.ErrorMessages;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	CustomerRepository customerRepository;
	@Autowired
	CouponRepository couponRepository;
	@Autowired
	CompanyRepository companyRepository;
	@Autowired
	ModelMapper modelMapper;

	@Override
	public CouponRest purchaseCoupon(String customerEmail, CouponRequestModel coupon) {
		
		// check for a valid coupon
		CouponRequestModel.isValid(coupon);
		
		if (coupon.getCompanyId() == null || coupon.getCompanyId().isBlank() || coupon.getCompanyId().isEmpty()) {
			// customer wants to purchase coupon without specifying from which company to purchase
			throw new ApplicationException(ErrorMessages.COMPANY_ID_ERROR.getErrorMessage());
		}
		
		if (coupon.getAmount() != 1) { 
			// only one coupon of this type per customer
			throw new ApplicationException(ErrorMessages.COUPON_MAX_ONCE.getErrorMessage());
		}
		
		// get company from which customer wants to purchase coupon
		CompanyEntity companyEntity = companyRepository.findByCompanyId(coupon.getCompanyId());		
		if (companyEntity == null) {
			throw new ApplicationException(ErrorMessages.COMPANY_ID_ERROR.getErrorMessage());
		}
		
		// find the requested coupon from company coupons
		CouponEntity requestedCoupon = null;
		for (CouponEntity couponEntity : companyEntity.getCoupons()) {
			if (couponEntity.getTitle().equalsIgnoreCase(coupon.getTitle())) {
				// requested coupon has been found
				requestedCoupon = couponEntity;
				break;
			}
		}
		
		if (requestedCoupon == null) {
			// requested coupon is not found among this company coupons
			throw new ApplicationException(ErrorMessages.COUPON_NOT_FOUND.getErrorMessage());
		}
		
		// get customer details
		CustomerEntity customerEntity = getCustomer(customerEmail);
		
		// check if customer already bought this coupon before
		for (CouponEntity couponEntity : customerEntity.getCoupons()) {
			if (couponEntity.getTitle().equalsIgnoreCase(coupon.getTitle())) {
				// customer already have this coupon
				throw new ApplicationException(ErrorMessages.COUPON_ALREADY_PURCHASED.getErrorMessage());
			}
		}
		
		// purchase coupon
//		requestedCoupon.getCustomers().add(customerEntity);
		customerEntity.getCoupons().add(requestedCoupon);
		
		// update coupon amount
		requestedCoupon.setAmount(requestedCoupon.getAmount() - 1);
		
		// update database
		customerRepository.save(customerEntity);
		CouponEntity updatedCoupon = couponRepository.save(requestedCoupon);
		
		return modelMapper.map(updatedCoupon, CouponRest.class);
	}

	@Override
	public List<CouponRest> getCustomerCoupons(String customerEmail) {
		CustomerEntity customerEntity = getCustomer(customerEmail);
		
		List<CouponRest> list = new ArrayList<>();
		for (CouponEntity couponEntity : customerEntity.getCoupons()) {
			CouponRest couponRest = modelMapper.map(couponEntity, CouponRest.class);
			if ( !list.contains(couponRest) ) {
				couponRest.setAmount(1);
				list.add(couponRest);
			}
		}

		return list;
	}

	@Override
	public List<CouponRest> getCustomerCoupons(String customerEmail, String value) {
		
		try {
			double price = Double.parseDouble(value);
			// client entered a number assume wants coupons by max price
			return getCustomerCoupons(customerEmail, price);
		} catch (NumberFormatException | NullPointerException e) {
			// client wants coupons by category
		}
		
		CustomerEntity customerEntity = getCustomer(customerEmail);
		
		List<CouponEntity> list = new ArrayList<>();
		for (CouponEntity couponEntity : customerEntity.getCoupons()) {
			if (couponEntity.getCategory().toString().equalsIgnoreCase(value)) {
				// coupons category name is not case sensitive
				list.add(couponEntity);
			}
		}
		
		Type listType = new TypeToken<List<CouponRest>>() {}.getType();
		
		return modelMapper.map(list, listType);
	}

	@Override
	public List<CouponRest> getCustomerCoupons(String customerEmail, double price) {

		CustomerEntity customerEntity = getCustomer(customerEmail);
		
		List<CouponEntity> list = new ArrayList<>();
		for (CouponEntity couponEntity : customerEntity.getCoupons()) {
			if (Double.compare(couponEntity.getPrice(), price) <= 0) {
				list.add(couponEntity);
			}
		}

		Type listType = new TypeToken<List<CouponRest>>() {}.getType();

		return modelMapper.map(list, listType);
	}

	@Override
	public CustomerRest getCustomerDetails(String customerEmail) {
		return modelMapper.map(customerRepository.findByEmail(customerEmail), CustomerRest.class);
	}
	
	/*********************************** Utility methods ***********************************/
	
	private CustomerEntity getCustomer(String customerEmail) {
		return customerRepository.findByEmail(customerEmail);
	}


}

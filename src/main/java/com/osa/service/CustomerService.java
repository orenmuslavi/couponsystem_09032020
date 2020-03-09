package com.osa.service;

import java.util.List;

import com.osa.ui.model.request.CouponRequestModel;
import com.osa.ui.model.response.CouponRest;
import com.osa.ui.model.response.CustomerRest;

public interface CustomerService {
	
	CouponRest purchaseCoupon(String customerId, CouponRequestModel coupon);
	
	List<CouponRest> getCustomerCoupons(String customerId);
	
	List<CouponRest> getCustomerCoupons(String customerId, String value);
	
//	List<CouponRest> getCustomerCoupons(String customerId, Category category);
	
	List<CouponRest> getCustomerCoupons(String customerId, double maxPrice);
	
	CustomerRest getCustomerDetails(String customerId);
	
	
}

package com.osa.service;

import java.util.List;

import com.osa.ui.model.request.CouponRequestModel;
import com.osa.ui.model.response.CompanyRest;
import com.osa.ui.model.response.CouponRest;

public interface CompanyService {
	
	CouponRest addCoupon(String email, CouponRequestModel coupon);
	CouponRest updateCoupon(String email, String couponId, CouponRequestModel coupon);
	void deleteCoupon(String email, String couponId);
	
	List<CouponRest> getCompanyCoupons(String email);
	CouponRest getCouponById(String id);
	List<CouponRest> getCompanyCoupons(String email, String value);
	
	CompanyRest getCompanyDetails(String email);

}

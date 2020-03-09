package com.osa.ui.model.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

public class CompanyRest {

	private String companyId;
	private String name;
	private String email;
	
	@JsonManagedReference
	private List<CouponRest> coupons;

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<CouponRest> getCoupons() {
		return coupons;
	}

	public void setCoupons(List<CouponRest> coupons) {
		this.coupons = coupons;
	}

}

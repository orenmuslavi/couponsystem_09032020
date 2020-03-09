package com.osa.ui.model.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

public class CustomerRest {

	private String customerId;
	private String firstName;
	private String lastName;
	private String email;
	
	@JsonManagedReference
	private List<CouponRest> coupons;

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
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

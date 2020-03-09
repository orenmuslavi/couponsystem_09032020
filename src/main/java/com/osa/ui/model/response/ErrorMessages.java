package com.osa.ui.model.response;

public enum ErrorMessages {
	
	TITLE_ERROR("Error: Title field is not valid"),
	DESCRIPTION_ERROR("Error: Description field is not valid"),
	START_DATE_ERROR("Error: Start date is before current date"),
	END_DATE_ERROR("Error: End date is before Start data"),
	AMOUNT_ERROR("Error: Amount should be grater then 0"),
	PRICE_ERROR("Error: Price is less then or equal to 0.0$"),
	CATEGORY_ERROR("Error: There is no such category"),
	COMPANY_ID_ERROR("Error: Company Id is required"),
	
	COUPON_NOT_FOUND("Error: Coupon has not been found"),
	COUPON_SAME_TITLE_EXISTS("Error: Coupon with same title already exists"),
	COUPON_AMOUNT_ZERO("Error: Sorry, no coupons left"),
	COUPON_ALREADY_PURCHASED("Error: You already have this coupon"),
	COUPON_MAX_ONCE("Error: Only one coupon of this type is allowed to be purchased"),
	
	
	RECORD_ALREADY_EXISTS("Record already exists"),
	RECORD_SAME_NAME_EXISTS("Record with same name already exists"),
	RECORD_SAME_EMAIL_EXISTS("Record with same email already exists"),
	RECORD_SAME_ID_EXISTS("Record with same id already exists"),
	MISSING_REQUIRED_FIELD("Missing required field"),
	NO_RECORD_FOUND("Record not found"),
	FORBID_NAME_AND_ID_CHANGE("Company name and ID change is forbidden"),
	CATEGORY_NOT_FOUND("Coupon from this category doesn't exists"),
	COUPON_EXPIRED("Coupon date expired"),
	INVALID_EMAIL("Error: email is not valid");
	
	private String errorMessage;

	private ErrorMessages(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
}

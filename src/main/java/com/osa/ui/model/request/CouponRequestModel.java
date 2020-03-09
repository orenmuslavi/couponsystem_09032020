package com.osa.ui.model.request;

import static com.osa.ui.model.response.ErrorMessages.*;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.osa.io.entity.Category;
import com.osa.ui.exceptions.ApplicationException;

public class CouponRequestModel {

	private String title;
	private String description;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate startDate;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate endDate;
	
	private int amount;
	private double price;
	private String image;
	private Category category;
	private String companyId;
	
	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public static boolean isValid(CouponRequestModel coupon) {
		if (coupon.title.isBlank() || coupon.title.isEmpty()) {
			throw new ApplicationException(TITLE_ERROR.getErrorMessage());
		}
		
		if (coupon.description.isBlank() || coupon.description.isEmpty()) {
			throw new ApplicationException(DESCRIPTION_ERROR.getErrorMessage());
		}
		
		if (coupon.getStartDate().isBefore(LocalDate.now())) {
			throw new ApplicationException(START_DATE_ERROR.getErrorMessage());
		}
		
		if (coupon.getEndDate().isBefore(coupon.getStartDate())) {
			throw new ApplicationException(END_DATE_ERROR.getErrorMessage());
		}
		
		if (coupon.getAmount() <= 0) {
			throw new ApplicationException(AMOUNT_ERROR.getErrorMessage());
		}
		
		if (Double.compare(coupon.getPrice(), 0) < 1) {
			throw new ApplicationException(PRICE_ERROR.getErrorMessage());
		}
		
		// FIXME: display proper error message
		if ( !Category.isCategoryValid(coupon.getCategory().toString()) ) {
			throw new ApplicationException(CATEGORY_ERROR.getErrorMessage());
		}
		
		return true; 
	}
	


}

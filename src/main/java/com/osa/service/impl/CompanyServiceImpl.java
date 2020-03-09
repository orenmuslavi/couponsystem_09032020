package com.osa.service.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.osa.io.entity.CompanyEntity;
import com.osa.io.entity.CouponEntity;
import com.osa.io.repositories.CompanyRepository;
import com.osa.io.repositories.CouponRepository;
import com.osa.service.CompanyService;
import com.osa.shared.Utils;
import com.osa.ui.exceptions.ApplicationException;
import com.osa.ui.model.request.CouponRequestModel;
import com.osa.ui.model.response.CompanyRest;
import com.osa.ui.model.response.CouponRest;
import com.osa.ui.model.response.ErrorMessages;

@Service
public class CompanyServiceImpl implements CompanyService {

	@Autowired 
	CompanyRepository companyRepository;
	@Autowired
	ModelMapper modelMapper; 
	@Autowired
	CouponRepository couponRepository;
	@Autowired
	Utils utils;
	final int idLength = 20;
	
	@Override
	public CouponRest addCoupon(String companyEmail, CouponRequestModel coupon) {
		
		// check if coupon is valid
		CouponRequestModel.isValid(coupon);
		
		CompanyEntity companyEntity = getCompany(companyEmail);
		
		// check if coupon with the same title already exists in this company coupons
		String couponTitle = coupon.getTitle();
		for (CouponEntity couponEntity : companyEntity.getCoupons()) {
			if (couponTitle.equalsIgnoreCase(couponEntity.getTitle())) {
				throw new ApplicationException(ErrorMessages.COUPON_SAME_TITLE_EXISTS.getErrorMessage());
			}
		}
		
		CouponEntity couponEntity = modelMapper.map(coupon, CouponEntity.class);
		couponEntity.setCompany(companyEntity);		// add new coupon to this company
		couponEntity.setCouponId(utils.generateId(idLength));	// generate random public id
		
		CouponEntity storedCoupon = couponRepository.save(couponEntity);
		
		CouponRest returnValue = modelMapper.map(storedCoupon, CouponRest.class);
		returnValue.setCustomers(new ArrayList<>()); // prevent null in response
		
		return returnValue; 
	}

	@Override
	public CouponRest updateCoupon(String companyEmail, String couponId, CouponRequestModel coupon) {
		
		CouponRequestModel.isValid(coupon);
		
		CompanyEntity companyEntity = getCompany(companyEmail);
		
		CouponEntity couponEntity = null;
		for (CouponEntity ce : companyEntity.getCoupons()) {
			if (ce.getCouponId().equals(couponId)) {
				couponEntity = ce;
				break;
			}
		}
		
		if (couponEntity == null) {
			// coupon with this provided couponId hasn't been found among company coupons
			throw new ApplicationException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		}
		
		if ( !couponEntity.getTitle().equalsIgnoreCase(coupon.getTitle()) ) {
			// original title requested to be changed
			for (CouponEntity ce : companyEntity.getCoupons()) {
				if (ce.getTitle().equalsIgnoreCase(coupon.getTitle())) {
					// there is already a coupon with this title, updated coupon is not valid
					throw new ApplicationException(ErrorMessages.COUPON_SAME_TITLE_EXISTS.getErrorMessage());
				}
			}
		}
		
		// update coupon
//		modelMapper.map(coupon, couponEntity);
		BeanUtils.copyProperties(coupon, couponEntity, "id", "couponId");
		
		return modelMapper.map(couponRepository.save(couponEntity), CouponRest.class);
	}

	@Override
	public void deleteCoupon(String companyEmail, String couponId) {
		
		CompanyEntity companyEntity = getCompany(companyEmail);
		
		CouponEntity deletedCoupon = new CouponEntity();
		for (CouponEntity couponEntity : companyEntity.getCoupons()) {
			if (couponEntity.getCouponId().equals(couponId)) {
				deletedCoupon = couponEntity;
				break;
			}
		}
		
		if (deletedCoupon.getCouponId() == null) {
			// coupon with provided id is not found
			throw new ApplicationException(ErrorMessages.COUPON_NOT_FOUND.getErrorMessage());
		}
		
		// delete coupon
		couponRepository.delete(deletedCoupon);
	}

	@Override
	public List<CouponRest> getCompanyCoupons(String companyEmail) {
		Type listType = new TypeToken<List<CouponRest>>() {}.getType();
		return modelMapper.map(getCompany(companyEmail).getCoupons(), listType);
	}

	@Override
	public List<CouponRest> getCompanyCoupons(String companyEmail, String value) {
		
		try {
			double price = Double.parseDouble(value);
			// client entered a number assume wants coupons by max price
			return getCompanyCoupons(companyEmail, price);
		} catch (NumberFormatException | NullPointerException e) {
			// client wants coupons by category
		}
		
		CompanyEntity companyEntity = getCompany(companyEmail);
		
		List<CouponEntity> list = new ArrayList<>();
		for (CouponEntity couponEntity : companyEntity.getCoupons()) {
			if (couponEntity.getCategory().toString().equalsIgnoreCase(value)) {
				// coupons category name is not case sensitive
				list.add(couponEntity);
			}
		}
		
		Type listType = new TypeToken<List<CouponRest>>() {}.getType();
		
		return modelMapper.map(list, listType);
	}

	private List<CouponRest> getCompanyCoupons(String companyEmail, double maxPrice) {
		
		CompanyEntity companyEntity = getCompany(companyEmail);
		
		List<CouponEntity> list = new ArrayList<>();
		for (CouponEntity couponEntity : companyEntity.getCoupons()) {
			if (Double.compare(couponEntity.getPrice(), maxPrice) <= 0) {
				list.add(couponEntity);
			}
		}
		
		Type listType = new TypeToken<List<CouponRest>>() {}.getType();
		
		return modelMapper.map(list, listType);
	}

	@Override
	public CompanyRest getCompanyDetails(String companyEmail) {
		return modelMapper.map(companyRepository.findByEmail(companyEmail), CompanyRest.class);
	}
	
	@Override
	public CouponRest getCouponById(String id) {
		CouponEntity couponEntity = couponRepository.findByCouponId(id); 
		if (couponEntity == null) {
			throw new ApplicationException(ErrorMessages.COUPON_NOT_FOUND.getErrorMessage());
		}
		return modelMapper.map(couponEntity, CouponRest.class);
	}
	
	/************ Utility methods ***************/
	private CompanyEntity getCompany(String companyEmail) {
		return companyRepository.findByEmail(companyEmail);
	}


}

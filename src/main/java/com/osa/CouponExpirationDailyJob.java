package com.osa;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.osa.io.entity.CompanyEntity;
import com.osa.io.entity.CouponEntity;
import com.osa.io.repositories.CompanyRepository;
import com.osa.service.CompanyService;

@Component
public class CouponExpirationDailyJob {

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private CompanyService companyService;

	@Scheduled(fixedRate = 1000 * 60 * 60 * 24)
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void checkCoupons() {
		List<CompanyEntity> companies = companyRepository.findAll();
		for (CompanyEntity company : companies) {
			List<CouponEntity> coupons = company.getCoupons();
			for (CouponEntity coupon : coupons) {
				if (coupon.getEndDate().isBefore(LocalDate.now())) {
					companyService.deleteCoupon(company.getEmail(), coupon.getCouponId());
				}
			}
		}
	}
}

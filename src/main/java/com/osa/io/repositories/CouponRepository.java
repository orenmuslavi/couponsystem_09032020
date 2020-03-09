package com.osa.io.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.osa.io.entity.CouponEntity;

@Repository
public interface CouponRepository extends JpaRepository<CouponEntity, Long> {

	List<CouponEntity> findByPriceLessThanEqual(double maxPrice);
	CouponEntity findByCouponId(String id);

}

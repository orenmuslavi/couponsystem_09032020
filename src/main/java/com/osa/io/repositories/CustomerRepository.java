package com.osa.io.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.osa.io.entity.CustomerEntity;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

	CustomerEntity findByEmail(String email);
	CustomerEntity findById(String customerId);
	CustomerEntity findByEmailAndPassword(String email, String password);
	
	CustomerEntity findByCustomerId(String customerId);

}

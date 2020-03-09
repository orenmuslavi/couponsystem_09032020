package com.osa.io.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.osa.io.entity.CompanyEntity;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {
	
	CompanyEntity findByName(String name);
	CompanyEntity findByEmail(String email);
	CompanyEntity findByCompanyId(String companyId);

}

package com.osa.security.services;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.osa.io.entity.CompanyEntity;
import com.osa.io.entity.CustomerEntity;
import com.osa.io.repositories.CompanyRepository;
import com.osa.io.repositories.CustomerRepository;
import com.osa.shared.Utils;
import com.osa.ui.model.response.ErrorMessages;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	CompanyRepository companyRepository;
	@Autowired
	CustomerRepository customerRepository; 
	@Autowired
	Utils utils;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		// try loading admin first
		if ("admin@admin.com".equals(email)) {
			return new UserDetailsImpl(
					"Admin", 
					email, 
					"$2a$10$wRrb0noQlAnIdPhb7TqtMuyhQ2fCG5kwfr4JDHu93bDY0zQsPJGmS", 
					Set.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
			
//			return new User("admin@admin.com", 
//					"$2a$10$wRrb0noQlAnIdPhb7TqtMuyhQ2fCG5kwfr4JDHu93bDY0zQsPJGmS", 
//					Set.of(new SimpleGrantedAuthority("ROLE_ADMIN"))); 
		}
		
		// load company 
		CompanyEntity companyEntity = companyRepository.findByEmail(email);
		if (companyEntity != null) {
//			return new User(companyEntity.getEmail(), companyEntity.getPassword(), companyEntity.getAuthorities());
			return UserDetailsImpl.build(companyEntity);
		}
		
		// load customer
		CustomerEntity customerEntity = customerRepository.findByEmail(email);
		if (customerEntity != null) {
//			return new User(customerEntity.getEmail(), customerEntity.getPassword(), customerEntity.getAuthorities());
			return UserDetailsImpl.build(customerEntity);
		}
		
		throw new UsernameNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

	}
	

}

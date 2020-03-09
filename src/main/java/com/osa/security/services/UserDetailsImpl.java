package com.osa.security.services;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.osa.io.entity.CompanyEntity;
import com.osa.io.entity.CustomerEntity;

public class UserDetailsImpl implements UserDetails {
	
	private static final long serialVersionUID = 3369494230202013686L;
	
	private String name;
	private String lastName = "";
	private String email;
//	@JsonIgnore
	private String password;
	private Collection<? extends GrantedAuthority> authorities;
	
	public UserDetailsImpl(
			String name, 
			String email, 
			String password,
			Collection<? extends GrantedAuthority> authorities) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.authorities = authorities;
	}

	public static UserDetailsImpl build(CompanyEntity companyEntity) {
		return new UserDetailsImpl(
				companyEntity.getName(), 
				companyEntity.getEmail(),
				companyEntity.getPassword(),
				companyEntity.getAuthorities());
	}
	
	public UserDetailsImpl(
			String name, 
			String lastName, 
			String email, 
			String password,
			Collection<? extends GrantedAuthority> authorities) {
		this(name, email, password, authorities);
		this.lastName = lastName;
	}
	
	public static UserDetailsImpl build(CustomerEntity customerEntity) {
		return new UserDetailsImpl(
				customerEntity.getFirstName(),
				customerEntity.getLastName(),
				customerEntity.getEmail(),
				customerEntity.getPassword(),
				customerEntity.getAuthorities());
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	@Override
	public String getUsername() {
		return this.name;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	
	

}

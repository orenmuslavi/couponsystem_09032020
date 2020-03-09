package com.osa.io.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Entity
@Table(name = "company")
public class CompanyEntity implements Serializable {

	private static final long serialVersionUID = 6090502062646312047L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private long id;

	@Column(nullable = false)
	private String companyId;

	@Column(length = 50, nullable = false)
	private String name;

	@Column(length = 150, nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String password;
	
	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "company")
	private List<CouponEntity> coupons;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
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

	public List<CouponEntity> getCoupons() {
		return coupons;
	}

	public void setCoupons(List<CouponEntity> coupons) {
		this.coupons = coupons;
	}
	
	public Set<GrantedAuthority> getAuthorities() {
		return Set.of(new SimpleGrantedAuthority("ROLE_COMPANY"));
	}

	@Override
	public String toString() {
		return "CompanyEntity [id=" + id + ", companyId=" + companyId + ", name=" + name + ", email=" + email
				+ ", password=" + password + ", coupons=" + coupons + "]";
	}
	
}

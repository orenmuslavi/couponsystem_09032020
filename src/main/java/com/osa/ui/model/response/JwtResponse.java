package com.osa.ui.model.response;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

public class JwtResponse {
	private String type = "Bearer";
	private String token;
	private String username;
	private String email;
	private Collection<? extends GrantedAuthority> authorities;

	public JwtResponse(String token, String username, String email,
			Collection<? extends GrantedAuthority> authorities) {
		this.token = token;
		this.username = username;
		this.email = email;
		this.authorities = authorities;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
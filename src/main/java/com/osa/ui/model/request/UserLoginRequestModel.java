package com.osa.ui.model.request;

public class UserLoginRequestModel {
	
	// every user of this application must provide
	// email and password when logging in
	// JSON payload will be converted to this class instance
	
	private String email;
	private String password;

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
}

package com.osa.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class AppProperties {
	
	/**
	 * read properties from application.properties file
	 */
	
	@Autowired
	private Environment environment;
	
	public String getTokenSecret() {
		return environment.getProperty("tokenSecret");
	}
	
	public long getTokenExpirationTime() {
		return Long.parseLong(environment.getProperty("tokenExpirationTime"));
	}
	
	
	
}

package com.osa.security;

import com.osa.SpringApplicationContext;

public class SecurityConstants {
	
	/**
	 *  Constants which are used by the authentication filter
	 */
	
//    public static final long EXPIRATION_TIME = 3600000 ; // 1 hour in milliseconds
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String REGISTER_URL = "/register/**";
    
    // will be used to generate encrypted value issued access token upon successful authorization
//    public static final String TOKEN_SECRET = "AT40V9w3sOul64pqP4YTRTS.aUJjYoYfxN39t5Icf2";
    
    // Instead of hard coding token secret we get it from application property file
    public static String getTokenSecret() {
    	AppProperties appProperties = (AppProperties) SpringApplicationContext.getBean("appProperties");
    	return appProperties.getTokenSecret();
    }
    
    public static long getTokenExpirationTime() {
    	AppProperties appProperties = (AppProperties) SpringApplicationContext.getBean("appProperties");
    	return appProperties.getTokenExpirationTime();
    }
    

}

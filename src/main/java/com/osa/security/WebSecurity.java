package com.osa.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.osa.security.jwt.JwtAuthEntryPoint;
import com.osa.security.jwt.JwtAuthTokenFilter;
import com.osa.security.services.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)	// enable method-level security
public class WebSecurity extends WebSecurityConfigurerAdapter {

	@Autowired	// will be injected by our own UserDetailsServiceImpl to help spring load users from database
	private UserDetailsServiceImpl userDetailsService; 
	
	@Autowired	// loaded user passwords will be decoded by this encoder
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Override	// specify how users will be looked up during authentication
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
		.userDetailsService(userDetailsService)	// interface which loads user-specific data. 
		.passwordEncoder(bCryptPasswordEncoder);
	}

	// configure how security is handled at the web level
	// Intercepting requests to ensure that the user has proper authority
	@Override 
	protected void configure(HttpSecurity http) throws Exception {
		http
		.cors()	// allow CORS by security
		.and()
		.csrf().disable()
		.authorizeRequests()
		.antMatchers(HttpMethod.GET, "/").permitAll()
		.antMatchers(HttpMethod.POST, "/login").permitAll()
		.antMatchers(HttpMethod.POST, SecurityConstants.REGISTER_URL).permitAll()	// sign-up page has a free access
		.anyRequest().authenticated()	// any request must be authenticated by the following filters
		.and()
		.exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
		
		// http session created when client communicate with server
		// for each http session client can be uniquely identified 
		// these sessions can cache some information about the request
		// which can make our Authorization header also cached 
		// we want with each Authorization header to re-authorize it.
		// Don't create http-sessions, prevents Authorization header

	}
	
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    
    @Autowired
    private JwtAuthEntryPoint unauthorizedHandler;

    @Bean
    public JwtAuthTokenFilter authenticationJwtTokenFilter() {
        return new JwtAuthTokenFilter();
    }
	
}

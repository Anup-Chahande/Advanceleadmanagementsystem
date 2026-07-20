package com.app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableMethodSecurity
@Configuration
public class Securityconfig {

	@Autowired
	Myuserdetialsservice myuserdettialsserviece;

	@Autowired
	JwtAuthFilter JwtAuthFilter;

	@Bean
	public SecurityFilterChain securityfilterchain(HttpSecurity security) throws Exception {

		return security

				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth.requestMatchers("/auth/**", "/forget/**", "/meta/**").permitAll()// .requestMatchers("/ADMIN/**").hasRole("ADMIN")
//		.requestMatchers("/EMPLOYEE/**").hasRole("EMPLOYEE")
//		.requestMatchers("/MANAGER/**").hasRole("MANAGER")

						.anyRequest().authenticated())
				.addFilterBefore(JwtAuthFilter,
						org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)

				.build();

	}

	@Bean
	public org.springframework.security.authentication.AuthenticationManager authenticationManager(
			org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration config)
			throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordencoder() {

		return new BCryptPasswordEncoder();

	}

	@Bean
	public AuthenticationProvider daoauthenticationprovider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider(myuserdettialsserviece);
		provider.setPasswordEncoder(passwordencoder());
		return provider;

	}

}

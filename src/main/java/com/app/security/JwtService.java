package com.app.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

	private static final String SECRET = "mysecretkeymysecretkeymysecretkey";

	private static final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 15; // 15 minutes

	private static final long REFRESH_TOKEN_EXPIRATION = 1000L * 60 * 60 * 24 * 7; // 7 days

	private Key getSignKey() {
		return Keys.hmacShaKeyFor(SECRET.getBytes());
	}

	public String generateAccessToken(String username) {

	    return Jwts.builder()
	            .setSubject(username)
	            .setIssuedAt(new Date())
	            .setExpiration(
	                    new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION)
	            )
	            .signWith(getSignKey(), SignatureAlgorithm.HS256)
	            .compact();
	}
	
	public String generateRefreshToken(String username) {

	    return Jwts.builder()
	            .setSubject(username)
	            .setIssuedAt(new Date())
	            .setExpiration(
	                    new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION)
	            )
	            .signWith(getSignKey(), SignatureAlgorithm.HS256)
	            .compact();
	}
	
	
	

	public String extractUsername(String token) {
		return extractAllClaims(token).getSubject();
	}

	public boolean isTokenValid(String token, String username) {
		return extractUsername(token).equals(username) && !isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {
		return extractAllClaims(token).getExpiration().before(new Date());
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
	}
}
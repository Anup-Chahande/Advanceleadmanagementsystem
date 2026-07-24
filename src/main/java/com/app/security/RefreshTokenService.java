package com.app.security;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.Entity.USER;
import com.app.Repositry.Userreop;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class RefreshTokenService {
	@Autowired
	private RefreshTokenRepository refreshTokenRepository;

	@Autowired
	private Userreop userrepo;

	@Autowired
	private JwtService jwtService;

	private String getDeviceName(HttpServletRequest request) {

		String userAgent = request.getHeader("User-Agent");

		if (userAgent == null) {
			return "UNKNOWN";
		}

		userAgent = userAgent.toLowerCase();

		if (userAgent.contains("android")) {
			return "ANDROID";
		}

		if (userAgent.contains("iphone") || userAgent.contains("ipad")) {
			return "IOS";
		}

		if (userAgent.contains("windows")) {
			return "WINDOWS";
		}

		if (userAgent.contains("mac")) {
			return "MAC";
		}

		if (userAgent.contains("linux")) {
			return "LINUX";
		}

		return "WEB";
	}

	private String getClientIp(HttpServletRequest request) {

		String xfHeader = request.getHeader("X-Forwarded-For");

		if (xfHeader != null && !xfHeader.isBlank()) {
			return xfHeader.split(",")[0];
		}

		return request.getRemoteAddr();
	}

	public RefreshToken createRefreshToken(String email, HttpServletRequest http) {
		USER user = userrepo.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
		RefreshToken refreshtoken = new RefreshToken();

		refreshtoken.setUser(user);
		refreshtoken.setToken(jwtService.generateRefreshToken(user.getEmail()));
		refreshtoken.setCreatedAt(LocalDateTime.now());
		refreshtoken.setExpiryDate(LocalDateTime.now().plusDays(7));
		refreshtoken.setRevoked(false);
		refreshtoken.setDeviceName(getDeviceName(http));
		refreshtoken.setIpAddress(getClientIp(http));
		return refreshTokenRepository.save(refreshtoken);

	}

	private boolean isExpired(RefreshToken refreshToken) {

		return refreshToken.getExpiryDate().isBefore(LocalDateTime.now());

	}

	public RefreshToken verifyRefreshToken(String token) {

	    RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
	            .orElseThrow(() -> new RuntimeException("Refresh Token not found"));


	    if (refreshToken.isRevoked()) {
	        throw new RuntimeException("Refresh Token has been revoked");
	    }


	    if (isExpired(refreshToken)) {

	        refreshTokenRepository.delete(refreshToken);

	        throw new RuntimeException("Refresh Token expired");
	    }


	    if (!jwtService.isTokenValid(
	            token,
	            refreshToken.getUser().getEmail()
	    )) {
	        throw new RuntimeException("Invalid Refresh Token");
	    }


	    return refreshToken;
	}
	
	public RefreshToken saveRefreshToken(RefreshToken refreshToken) {
	    return refreshTokenRepository.save(refreshToken);
	}

	public void deleteRefreshToken(USER user) {

		refreshTokenRepository.deleteByUser(user);

	}

}

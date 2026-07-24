package com.app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.app.Entity.USER;
import com.app.Repositry.Userreop;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private CookieService cookieService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private RefreshTokenService refreshTokenService;

	@Autowired
	private JwtService jwtService;
	
	@Autowired
	Userreop userrepo;

	@PostMapping("/login")
	 public AuthResponse login(
	        @RequestBody AuthRequest request,
	        HttpServletRequest httpRequest,
	        HttpServletResponse response) {
	    authenticationManager.authenticate(
	        new UsernamePasswordAuthenticationToken(
	            request.getUsername(),
	            request.getPassword()
	        )
	    );

	    String accessToken =
	            jwtService.generateAccessToken(request.getUsername());

	    RefreshToken refreshToken =
	            refreshTokenService.createRefreshToken(request.getUsername(),httpRequest);
	    response.addHeader(
	            "Set-Cookie",
	            cookieService
	                    .createAccessTokenCookie(accessToken)
	                    .toString()
	    );

	    response.addHeader(
	            "Set-Cookie",
	            cookieService
	                    .createRefreshTokenCookie(
	                            refreshToken.getToken()
	                    )
	                    .toString()
	    );
	    
	    USER user = userrepo.findByEmail(request.getUsername())
	            .orElseThrow(() -> new RuntimeException("User not found"));
	    
	    if (user.getCompany() != null && !user.getCompany().isActive()) {
	        throw new RuntimeException("Your company has been deactivated. Please contact support.");
	    }

	    return new AuthResponse(
	    	    accessToken,
	    	    refreshToken.getToken(),
	    	    user.getUsername(),
	    	    user.getEmail(),
	    	    user.getRole().getName()
	    	);
	}
	
	
	@GetMapping("/me")
	@PreAuthorize("isAuthenticated()")
	public AuthResponse me() {

	    String email = SecurityContextHolder
	            .getContext()
	            .getAuthentication()
	            .getName();

	    USER user = userrepo.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    return new AuthResponse(
	           
	            user.getUsername(),
	            user.getEmail(),
	            user.getRole().getName()
	    );
	}
	
	@Transactional
	@PostMapping("/refresh")
	public AuthResponse refresh(
			HttpServletResponse response,
	        HttpServletRequest httpRequest) {

		String refreshTokenValue = null;

		if (httpRequest.getCookies() != null) {

		    for (Cookie cookie : httpRequest.getCookies()) {

		        if ("refreshToken".equals(cookie.getName())) {

		            refreshTokenValue = cookie.getValue();

		            break;
		        }
		    }
		}

		if (refreshTokenValue == null) {
		    throw new RuntimeException("Refresh Token Missing");
		}
	    // 1. Verify old refresh token
		RefreshToken oldRefreshToken =
		        refreshTokenService.verifyRefreshToken(
		                refreshTokenValue
		        );


	    USER user = oldRefreshToken.getUser();


	    // 2. Revoke old refresh token
	    oldRefreshToken.setRevoked(true);

	    refreshTokenService.saveRefreshToken(oldRefreshToken);



	    String newAccessToken =
	            jwtService.generateAccessToken(
	                    user.getEmail()
	            );



	    RefreshToken newRefreshToken =
	            refreshTokenService.createRefreshToken(
	                    user.getEmail(),
	                    httpRequest
	            );

	    response.addHeader(
	            "Set-Cookie",
	            cookieService
	                    .createAccessTokenCookie(newAccessToken)
	                    .toString()
	    );

	    response.addHeader(
	            "Set-Cookie",
	            cookieService
	                    .createRefreshTokenCookie(
	                            newRefreshToken.getToken()
	                    )
	                    .toString()
	    );
	    

	    return new AuthResponse(
	            newAccessToken,
	            newRefreshToken.getToken(),
	            user.getUsername(),
	            user.getEmail(),
	            user.getRole().getName()
	    );
	}
	
	
	
	// DTO inside
	public static class AuthRequest {
		private String username;
		private String password;

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password; 
		}
	}
	
	
	public static class AuthResponse {

		private String accessToken;
		private String refreshToken;
	    private String username;
	    private String email;
	    private String role;
		public AuthResponse(String accessToken, String refreshToken, String username, String email, String role) {
			super();
			this.accessToken = accessToken;
			this.refreshToken = refreshToken;
			this.username = username;
			this.email = email;
			this.role = role;
		}
		public AuthResponse(String username, String email, String role) {
		    this.username = username;
		    this.email = email;
		    this.role = role;
		}
		public String getAccessToken() {
			return accessToken;
		}
		
		public String getRefreshToken() {
			return refreshToken;
		}
		
		public String getUsername() {
			return username;
		}
		
		public String getEmail() {
			return email;
		}
		
		public String getRole() {
			return role;
		}
		

	    
	    
	}
}
package com.app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.app.Entity.USER;
import com.app.Repositry.Userreop;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtService jwtService;
	
	@Autowired
	Userreop userrepo;

	@PostMapping("/login")
	public AuthResponse login(@RequestBody AuthRequest request) {

	    authenticationManager.authenticate(
	        new UsernamePasswordAuthenticationToken(
	            request.getUsername(),
	            request.getPassword()
	        )
	    );

	    String token = jwtService.generateToken(request.getUsername());

	    USER user = userrepo.findByEmail(request.getUsername())
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    return new AuthResponse(
	        token,
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
	            null,
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

	    private String token;
	    private String username;
	    private String email;
	    private String role;

	    public AuthResponse(String token, String username, String email, String role) {
	        this.token = token;
	        this.username = username;
	        this.email = email;
	        this.role = role;
	    }

	    public String getToken() { return token; }
	    public String getUsername() { return username; }
	    public String getEmail() { return email; }
	    public String getRole() { return role; }
	}
}
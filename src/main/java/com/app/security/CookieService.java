package com.app.security;


import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
public class CookieService {

    // Access Token Cookie
    public ResponseCookie createAccessTokenCookie(String token) {

        return ResponseCookie.from("accessToken", token)
                .httpOnly(true)
                .secure(true)                .path("/")
                .maxAge(15 * 60)        // 15 minutes
                .sameSite("Lax")
                .build();
    }

    // Refresh Token Cookie
    public ResponseCookie createRefreshTokenCookie(String token) {

        return ResponseCookie.from("refreshToken", token)
                .httpOnly(true)
                .secure(true)                .path("/")
                .maxAge(7 * 24 * 60 * 60)   // 7 days
                .sameSite("Lax")
                .build();
    }

    // Remove Access Cookie
    public ResponseCookie deleteAccessTokenCookie() {

        return ResponseCookie.from("accessToken", "")
                .httpOnly(true)
                .secure(true)                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();
    }

    // Remove Refresh Cookie
    public ResponseCookie deleteRefreshTokenCookie() {

        return ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();
    }

}
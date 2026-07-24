package com.app.security;


import java.time.LocalDateTime;

import com.app.Entity.USER;

import jakarta.persistence.*;

@Entity
@Table(name = "refresh_token")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 500)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private boolean revoked;

    @Column(length = 100)
    private String deviceName;

    @Column(length = 50)
    private String ipAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private USER user;

	public RefreshToken() {
		super();
	}

	public RefreshToken(Long id, String token, LocalDateTime expiryDate, LocalDateTime createdAt, boolean revoked,
			String deviceName, String ipAddress, USER user) {
		super();
		this.id = id;
		this.token = token;
		this.expiryDate = expiryDate;
		this.createdAt = createdAt;
		this.revoked = revoked;
		this.deviceName = deviceName;
		this.ipAddress = ipAddress;
		this.user = user;
	}

	@Override
	public String toString() {
		return "RefreshToken [id=" + id + ", token=" + token + ", expiryDate=" + expiryDate + ", createdAt=" + createdAt
				+ ", revoked=" + revoked + ", deviceName=" + deviceName + ", ipAddress=" + ipAddress + ", user=" + user
				+ "]";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public LocalDateTime getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(LocalDateTime expiryDate) {
		this.expiryDate = expiryDate;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public boolean isRevoked() {
		return revoked;
	}

	public void setRevoked(boolean revoked) {
		this.revoked = revoked;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public USER getUser() {
		return user;
	}

	public void setUser(USER user) {
		this.user = user;
	}
	
	




}
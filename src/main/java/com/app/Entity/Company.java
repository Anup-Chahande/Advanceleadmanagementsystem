package com.app.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "company")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true)
    private String companyName;

    @Column(nullable=false)
    private String email;

    private String phone;

    private String address;

    private boolean active = true;

	public Company() {
		super();
	}

	public Company(Long id, String companyName, String email, String phone, String address, boolean active) {
		super();
		this.id = id;
		this.companyName = companyName;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.active = active;
	}

	@Override
	public String toString() {
		return "Company [id=" + id + ", companyName=" + companyName + ", email=" + email + ", phone=" + phone
				+ ", address=" + address + ", active=" + active + "]";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	


}
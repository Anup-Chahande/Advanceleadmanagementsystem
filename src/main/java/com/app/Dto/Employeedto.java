package com.app.Dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class Employeedto {
	private Long id;
	@NotBlank(message = "username cannot be empty")
	private String username;
	
	@NotBlank(message = "Email cannot be empty")
	@Email(message = "Enter valid email")
	private String email;
	
	@NotBlank(message = "City cannot be empty")
	private String city;

	@NotBlank(message = "Password cannot be empty")
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,}$", message = "Weak password")
	private String password;
	
	@NotNull(message = "dob is cannot be empty")
	private LocalDate dob;
	
	@NotBlank(message = "number cannot be empty")
	private String number;
	
	@NotBlank(message = "department cannot be empty")
	private String department;
	
	@NotBlank(message = "designation cannot be empty")
	private String designation;
	
	@NotNull(message = "joiningDate is cannot be empty")
	private LocalDate joiningDate;

	private Boolean active;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public LocalDate getJoiningDate() {
		return joiningDate;
	}

	public void setJoiningDate(LocalDate joiningDate) {
		this.joiningDate = joiningDate;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	

}

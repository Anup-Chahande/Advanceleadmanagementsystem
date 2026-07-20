package com.app.Entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;

	@Column(unique = true)
	private String email;
	private String city;
	private LocalDate dob;
	private String number;
	private String department;
	private String designation;
	private LocalDate joiningDate;
	private Boolean active = true;

	@OneToOne
	@JoinColumn(name = "user_id")
	@JsonIgnore
	private USER user;

	@ManyToOne
	@JoinColumn(name = "manager_id")
	@JsonIgnore
	private Manager manager;

	public Employee() {
		super();

	}

	public Employee(Long id, String name, String email, String city, LocalDate dob, String number, String department,
			String designation, LocalDate joiningDate, Boolean active, USER user, Manager manager) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.city = city;
		this.dob = dob;
		this.number = number;
		this.department = department;
		this.designation = designation;
		this.joiningDate = joiningDate;
		this.active = active;
		this.user = user;
		this.manager = manager;
	}

	@Override
	public String toString() {
		return "Employee [id=" + id + ", name=" + name + ", email=" + email + ", city=" + city + ", dob=" + dob
				+ ", number=" + number + ", department=" + department + ", designation=" + designation
				+ ", joiningDate=" + joiningDate + ", active=" + active + ", user=" + user + ", manager=" + manager
				+ "]";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public USER getUser() {
		return user;
	}

	public void setUser(USER user) {
		this.user = user;
	}

	public Manager getManager() {
		return manager;
	}

	public void setManager(Manager manager) {
		this.manager = manager;
	}

}

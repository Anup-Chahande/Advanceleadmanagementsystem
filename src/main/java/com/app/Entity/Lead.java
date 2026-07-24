package com.app.Entity;

import java.time.LocalDate;

import com.app.Enum.LeadStatus;
import com.app.Enum.Leadtype;
import com.app.Enum.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Leads")
public class Lead {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	private String city;
	private LocalDate dob;
	private String email;
	private String area;
	private String phone;
	private Double customerBudget;
	private String socialMediaName;
	private String remark;
	private String executedBy;

	@Enumerated(EnumType.STRING)
	private Leadtype leadtype;

	@Enumerated(EnumType.STRING)
	private LeadStatus leadstatus;

	@Enumerated(EnumType.STRING)
	private Status staus;

	@ManyToOne
	private USER assignedTo;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="company_id")
	@JsonIgnore
	private Company company;


	public Lead() {
		super();
		// TODO Auto-generated constructor stub
	}


	public Lead(Long id, String name, String city, LocalDate dob, String email, String area, String phone,
			Double customerBudget, String socialMediaName, String remark, String executedBy, Leadtype leadtype,
			LeadStatus leadstatus, Status staus, USER assignedTo, Company company) {
		super();
		this.id = id;
		this.name = name;
		this.city = city;
		this.dob = dob;
		this.email = email;
		this.area = area;
		this.phone = phone;
		this.customerBudget = customerBudget;
		this.socialMediaName = socialMediaName;
		this.remark = remark;
		this.executedBy = executedBy;
		this.leadtype = leadtype;
		this.leadstatus = leadstatus;
		this.staus = staus;
		this.assignedTo = assignedTo;
		this.company = company;
	}


	@Override
	public String toString() {
		return "Lead [id=" + id + ", name=" + name + ", city=" + city + ", dob=" + dob + ", email=" + email + ", area="
				+ area + ", phone=" + phone + ", customerBudget=" + customerBudget + ", socialMediaName="
				+ socialMediaName + ", remark=" + remark + ", executedBy=" + executedBy + ", leadtype=" + leadtype
				+ ", leadstatus=" + leadstatus + ", staus=" + staus + ", assignedTo=" + assignedTo + ", company="
				+ company + "]";
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


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getArea() {
		return area;
	}


	public void setArea(String area) {
		this.area = area;
	}


	public String getPhone() {
		return phone;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}


	public Double getCustomerBudget() {
		return customerBudget;
	}


	public void setCustomerBudget(Double customerBudget) {
		this.customerBudget = customerBudget;
	}


	public String getSocialMediaName() {
		return socialMediaName;
	}


	public void setSocialMediaName(String socialMediaName) {
		this.socialMediaName = socialMediaName;
	}


	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}


	public String getExecutedBy() {
		return executedBy;
	}


	public void setExecutedBy(String executedBy) {
		this.executedBy = executedBy;
	}


	public Leadtype getLeadtype() {
		return leadtype;
	}


	public void setLeadtype(Leadtype leadtype) {
		this.leadtype = leadtype;
	}


	public LeadStatus getLeadstatus() {
		return leadstatus;
	}


	public void setLeadstatus(LeadStatus leadstatus) {
		this.leadstatus = leadstatus;
	}


	public Status getStaus() {
		return staus;
	}


	public void setStaus(Status staus) {
		this.staus = staus;
	}


	public USER getAssignedTo() {
		return assignedTo;
	}


	public void setAssignedTo(USER assignedTo) {
		this.assignedTo = assignedTo;
	}


	public Company getCompany() {
		return company;
	}


	public void setCompany(Company company) {
		this.company = company;
	}

	
	

	
	
	
	
}

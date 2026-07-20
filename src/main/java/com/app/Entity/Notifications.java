package com.app.Entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
@Entity
public class Notifications {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
	private String message;
	private boolean isread=false;
	private LocalDate createdat= LocalDate.now();
	
	@ManyToOne
	@JsonIgnore
	private USER user;

	public Notifications() {
		super();
	}

	public Notifications(Long id, String title, String message, boolean read, LocalDate createdat, USER user) {
		super();
		this.id = id;
		this.title = title;
		this.message = message;
		this.isread = read;
		this.createdat = createdat;
		this.user = user;
	}

	@Override
	public String toString() {
		return "Notifications [id=" + id + ", title=" + title + ", message=" + message + ", read=" + isread
				+ ", createdat=" + createdat + ", user=" + user + "]";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isRead() {
		return isread;
	}

	public void setRead(boolean read) {
		this.isread = read;
	}

	public LocalDate getCreatedat() {
		return createdat;
	}

	public void setCreatedat(LocalDate createdat) {
		this.createdat = createdat;
	}

	public USER getUser() {
		return user;
	}

	public void setUser(USER user) {
		this.user = user;
	}
	
	
	
	
	
	
	
}

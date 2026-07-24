package com.app.Dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class CompanyAdminDto {

    @NotBlank(message = "Username is required")
    private String username;

    @Email(message = "Invalid email")
    private String email;

    @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$",
        message = "Password must contain 8 characters, one uppercase, one number and one special character"
    )
    private String password;

    public CompanyAdminDto() {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
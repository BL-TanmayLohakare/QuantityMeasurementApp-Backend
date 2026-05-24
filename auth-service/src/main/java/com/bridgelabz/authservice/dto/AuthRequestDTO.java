package com.bridgelabz.authservice.dto;

public class AuthRequestDTO {
    private String email;
    private String password;
    private String name; 

    public AuthRequestDTO() {}

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}

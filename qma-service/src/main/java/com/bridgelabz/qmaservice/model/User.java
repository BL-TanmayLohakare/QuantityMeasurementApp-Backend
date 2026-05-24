package com.bridgelabz.qmaservice.model;

public class User {
    private Long id;
    private String email;
    private String name;
    private String provider;

    public User() {}

    public User(String email, String name, String provider) {
        this.email = email;
        this.name = name;
        this.provider = provider;
    }

    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }
}

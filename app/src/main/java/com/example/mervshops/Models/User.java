package com.example.mervshops.Models;

public class User {
    private String photo;
    private  int id;
    private String name;
    private String email;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getUsername() {
        return name;
    }

    public String getEmail() {
        return email;
    }
    public void setUsername(String name) {
        this.name = name;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }
    public void setPhoto(String photo) {
        this.photo = photo;
    }
}

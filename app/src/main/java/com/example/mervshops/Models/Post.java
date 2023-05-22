package com.example.mervshops.Models;

public class Post {
    private int id,likes,commandes;
    private String date,desc,photo;
    private User user;
    private boolean selflike;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getCommandes() {
        return commandes;
    }

    public void setCommandes(int commandes) {
        this.commandes = commandes;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isSelflike() {
        return selflike;
    }

    public void setSelflike(boolean selflike) {
        this.selflike = selflike;
    }
}

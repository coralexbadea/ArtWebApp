package com.example.artapp.domain;

import javax.persistence.*;

@Entity
@Table(name="profile")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pid;

    private String fullName;
    private int age;
    private String favouriteArtist;
    private Style favouriteSyle;

    @OneToOne( cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true ,mappedBy = "profile")
    private User user;

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getFavouriteArtist() {
        return favouriteArtist;
    }

    public void setFavouriteArtist(String favouriteArtist) {
        this.favouriteArtist = favouriteArtist;
    }

    public Style getFavouriteSyle() {
        return favouriteSyle;
    }

    public void setFavouriteSyle(Style favouriteSyle) {
        this.favouriteSyle = favouriteSyle;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

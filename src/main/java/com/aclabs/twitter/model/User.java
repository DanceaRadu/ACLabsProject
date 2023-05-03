package com.aclabs.twitter.model;

import com.aclabs.twitter.model.Follow;
import com.aclabs.twitter.model.Like;
import com.aclabs.twitter.model.Post;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="users")
public class User {

    @Id
    @Column(name="username", length = 50, nullable = false)
    private String username;
    @Column(name="first_name", length=50, nullable=false)
    private String firstName;
    @Column(name="last_name", length=50, nullable=false)
    private  String lastName;
    @Column(name="email", length=50, nullable=false)
    private String email;
    @Column(name="password", length=100, nullable=false)
    private String password;

    @JsonManagedReference (value = "post")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "poster", cascade = CascadeType.REMOVE)
    private List<Post> posts;

    @JsonManagedReference (value = "followerReference")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "follower", cascade = CascadeType.REMOVE)
    private List<Follow> follows;

    @JsonManagedReference (value = "followedReference")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "followed", cascade = CascadeType.REMOVE)
    private List<Follow> followers;

    @JsonManagedReference (value = "userLikeReference")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "liker", cascade = CascadeType.REMOVE)
    private List<Like> likedPosts;

    public User() {}

    public User(String username, String firstName, String lastName, String email, String password, List<Post> posts, List<Follow> follows, List<Follow> followers, List<Like> likedPosts) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.posts = posts;
        this.follows = follows;
        this.followers = followers;
        this.likedPosts = likedPosts;
    }

    public User(String username) {this.username = username;}

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPosts(List<Post> posts){
        this.posts = posts;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public List<Post> getPosts() { return posts; }

}

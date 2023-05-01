package com.aclabs.twitter.user;

import com.aclabs.twitter.post.Post;
import jakarta.persistence.*;

import java.util.ArrayList;
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
    @Column(name="salt", length=100, nullable=false)
    private String salt;
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name="post_id")
    @Column(name="posts")
    private List<Post> posts;

    public User() {

    }

    public User(String username, String firstName, String lastName, String email, String password, String salt, List<Post> posts) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.salt = salt;
        this.posts = posts;
    }

    public User(String username) {
        this.username = username;
    }

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

    public void setSalt(String salt) {
        this.salt = salt;
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

    public String getSalt() {
        return salt;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void addPost(Post post) {
        if(post != null) {
            if(posts == null) posts = new ArrayList<>();
            posts.add(post);
        }
    }
}

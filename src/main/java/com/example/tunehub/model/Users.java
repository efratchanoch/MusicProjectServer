package com.example.tunehub.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Users {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String password;

    private String email;

    private String description;

    private UserType userType;

    private LocalDate createdAt;

    private LocalDate editedIn; //תאריך עריכה

    private boolean active;

//    private String teacherIdentity;

    @ManyToOne
    private Teacher teacher;


    // צריכים לבדוק סוג תמונה ושמע
    //    private String imageUrl;
    //


    //    // 4גג
    //    private String locationUrl;

    @OneToMany(mappedBy = "user")
    private List<SheetMusic> sheetMusic;

    @OneToMany(mappedBy = "user")
    private List<Post> posts;

    @OneToMany(mappedBy = "user")
    private List<Comment> comments;


    public Users() {
    }

    public Users(Long id, String name, String password, String email, String description, UserType userType, LocalDate createdAt, LocalDate editedIn, boolean active, Teacher teacher, List<SheetMusic> sheetMusic, List<Post> posts, List<Comment> comments) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.description = description;
        this.userType = userType;
        this.createdAt = createdAt;
        this.editedIn = editedIn;
        this.active = active;
        this.teacher = teacher;
        this.sheetMusic = sheetMusic;
        this.posts = posts;
        this.comments = comments;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getEditedIn() {
        return editedIn;
    }

    public void setEditedIn(LocalDate editedIn) {
        this.editedIn = editedIn;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public List<SheetMusic> getSheetMusic() {
        return sheetMusic;
    }

    public void setSheetMusic(List<SheetMusic> sheetMusic) {
        this.sheetMusic = sheetMusic;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}

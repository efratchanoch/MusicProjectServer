package com.example.tunehub.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Users {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String password;

    private String email;

    @Column(columnDefinition = "TEXT")
    private String description;

    private EUserType EUserType;

    private LocalDate createdAt;

    private LocalDate editedIn;

    private boolean isActive = false;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastActivityTimestamp;

    private String city;

    private String country;

    @ManyToMany
    private List<Instrument> instrumentsUsers;

    @ManyToOne
    private Teacher teacher;

    private String imageProfilePath;

    @OneToMany(mappedBy = "user")
    private List<SheetMusic> sheetsMusic;

    @OneToMany(mappedBy = "user")
    private List<Post> posts;

    @OneToMany(mappedBy = "user")
    private List<Comment> comments;

    @ManyToMany(mappedBy = "mentionedUsers")
    private Set<Post> mentionedInPosts = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Notification> receivedNotifications;

    @OneToMany(mappedBy = "actor")
    private List<Notification> sentNotifications;

    @Column(nullable = false)
    private int followerCount = 0;

    //Security
    @ManyToMany
    private Set<Role> roles = new HashSet<>();

    public Users() {
    }

    public Users(Long id, String name, String password, String email, String description, EUserType EUserType, LocalDate createdAt, LocalDate editedIn, boolean isActive, Date lastActivityTimestamp, String city, String country, List<Instrument> instrumentsUsers, Teacher teacher, String imageProfilePath, List<SheetMusic> sheetsMusic, List<Post> posts, List<Comment> comments, Set<Post> mentionedInPosts, List<Notification> receivedNotifications, List<Notification> sentNotifications, int followerCount, Set<Role> roles) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.description = description;
        this.EUserType = EUserType;
        this.createdAt = createdAt;
        this.editedIn = editedIn;
        this.isActive = isActive;
        this.lastActivityTimestamp = lastActivityTimestamp;
        this.city = city;
        this.country = country;
        this.instrumentsUsers = instrumentsUsers;
        this.teacher = teacher;
        this.imageProfilePath = imageProfilePath;
        this.sheetsMusic = sheetsMusic;
        this.posts = posts;
        this.comments = comments;
        this.mentionedInPosts = mentionedInPosts;
        this.receivedNotifications = receivedNotifications;
        this.sentNotifications = sentNotifications;
        this.followerCount = followerCount;
        this.roles = roles;
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

    public EUserType getEUserType() {
        return EUserType;
    }

    public void setEUserType(EUserType EUserType) {
        this.EUserType = EUserType;
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
        return isActive;
    }

    public void setIsActive(boolean active) {
        isActive = active;
    }

    public Date getLastActivityTimestamp() {
        return lastActivityTimestamp;
    }


    public void setLastActivityTimestamp(Date lastActivityTimestamp) {
        this.lastActivityTimestamp = lastActivityTimestamp;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<Instrument> getInstrumentsUsers() {
        return instrumentsUsers;
    }

    public void setInstrumentsUsers(List<Instrument> instrumentsUsers) {
        this.instrumentsUsers = instrumentsUsers;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public String getImageProfilePath() {
        return imageProfilePath;
    }

    public void setImageProfilePath(String imageProfilePath) {
        this.imageProfilePath = imageProfilePath;
    }

    public List<SheetMusic> getSheetsMusic() {
        return sheetsMusic;
    }

    public void setSheetsMusic(List<SheetMusic> sheetsMusic) {
        this.sheetsMusic = sheetsMusic;
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

    public Set<Post> getMentionedInPosts() {
        return mentionedInPosts;
    }

    public void setMentionedInPosts(Set<Post> mentionedInPosts) {
        this.mentionedInPosts = mentionedInPosts;
    }

    public List<Notification> getReceivedNotifications() {
        return receivedNotifications;
    }

    public void setReceivedNotifications(List<Notification> receivedNotifications) {
        this.receivedNotifications = receivedNotifications;
    }

    public List<Notification> getSentNotifications() {
        return sentNotifications;
    }

    public void setSentNotifications(List<Notification> sentNotifications) {
        this.sentNotifications = sentNotifications;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }


}

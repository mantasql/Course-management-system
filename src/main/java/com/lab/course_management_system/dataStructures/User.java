package com.lab.course_management_system.dataStructures;

import java.io.Serializable;
import java.time.LocalDate;

public abstract class User implements Serializable {
    private int id;
    private String userName;
    private String password;
    private LocalDate dateCreated;
    private LocalDate dateModified;
    private boolean isActive;
    private UserType userType;

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
        this.dateCreated = LocalDate.now();
        this.dateModified = LocalDate.now();
        this.userType = UserType.STUDENT;
        this.isActive = true;
    }

    public User(int id) {
        this.id = id;
    }

    public User(int id, String userName, String password, UserType userType) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.userType = userType;
    }

    public User(int id, UserType userType) {
        this.id = id;
        this.userType = userType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public LocalDate getDateModified() {
        return dateModified;
    }

    public void setDateModified(LocalDate dateModified) {
        this.dateModified = dateModified;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }
}

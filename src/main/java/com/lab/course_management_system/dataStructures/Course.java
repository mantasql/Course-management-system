package com.lab.course_management_system.dataStructures;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class Course implements Serializable {
    private int id;
    private String courseName;
    private String courseDescription;
    private ArrayList<Folder> folders = new ArrayList<>();
    private ArrayList<Folder> allFolders = new ArrayList<>();
    private ArrayList<User> moderators = new ArrayList<>();
    private ArrayList<Person> students = new ArrayList<>();
    private LocalDate dateCreated;

    public Course(String courseName, String courseDescription, ArrayList<User> moderators) {
        this.courseName = courseName;
        this.courseDescription = courseDescription;
        this.moderators = moderators;
        this.dateCreated = LocalDate.now();
    }

    public Course(int id, String courseName, String courseDescription, LocalDate dateCreated) {
        this.id = id;
        this.courseName = courseName;
        this.courseDescription = courseDescription;
        this.dateCreated = dateCreated;
    }

    public ArrayList<Folder> getFolders() {
        return folders;
    }

    public void setFolders(ArrayList<Folder> folders) {
        this.folders = folders;
    }

    public ArrayList<Folder> getAllFolders() {
        return allFolders;
    }

    public void setAllFolders(ArrayList<Folder> allFolders) {
        this.allFolders = allFolders;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public ArrayList<User> getModerators() {
        return moderators;
    }

    public void setModerators(ArrayList<User> moderators) {
        this.moderators = moderators;
    }

    public ArrayList<Person> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<Person> students) {
        this.students = students;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }
}

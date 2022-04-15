package com.lab.course_management_system.dataStructures;

import java.util.ArrayList;

public class CourseManagementSystem {
    private User loggedInUser;
    private ArrayList<Course> courses;

    public CourseManagementSystem(User loggedInUser, ArrayList<Course> courses) {
        this.loggedInUser = loggedInUser;
        this.courses = courses;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<Course> courses) {
        this.courses = courses;
    }
}

package com.lab.course_management_system.dataStructures;

import java.time.LocalDate;

public class File {
    private String name;
    private String location;
    private LocalDate dateCreated;

    public File() {
    }

    public File(String name, String location) {
        this.name = name;
        this.location = location;
        dateCreated = LocalDate.now();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }
}

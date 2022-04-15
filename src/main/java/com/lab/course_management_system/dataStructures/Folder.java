package com.lab.course_management_system.dataStructures;

import java.time.LocalDate;
import java.util.ArrayList;

public class Folder {
    private int id;
    private String title;
    private LocalDate dateCreated;
    private LocalDate dateModified;
    private Folder parentFolder;
    private ArrayList<Folder> subFolders = new ArrayList<>();
    private ArrayList<File> folderFiles = new ArrayList<>();

    public Folder() {
    }

    public Folder(String title,Folder parentFolder) {
        this.title = title;
        dateCreated = LocalDate.now();
        this.dateModified = LocalDate.now();
        this.parentFolder = parentFolder;
    }

    public Folder(int id, String title, LocalDate dateCreated, LocalDate dateModified, ArrayList<Folder> subFolders, ArrayList<File> folderFiles) {
        this.id = id;
        this.title = title;
        this.dateCreated = dateCreated;
        this.dateModified = dateModified;
        this.subFolders = subFolders;
        this.folderFiles = folderFiles;
    }

    public Folder(int id, String title, LocalDate dateCreated, LocalDate dateModified, Folder parentFolder) {
        this.id = id;
        this.title = title;
        this.dateCreated = dateCreated;
        this.dateModified = dateModified;
        this.parentFolder = parentFolder;
    }

    public Folder getParentFolder() {
        return parentFolder;
    }

    public void setParentFolder(Folder parentFolder) {
        this.parentFolder = parentFolder;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public ArrayList<Folder> getSubFolders() {
        return subFolders;
    }

    public void setSubFolders(ArrayList<Folder> subFolders) {
        this.subFolders = subFolders;
    }

    public ArrayList<File> getFolderFiles() {
        return folderFiles;
    }

    public void setFolderFiles(ArrayList<File> folderFiles) {
        this.folderFiles = folderFiles;
    }
}

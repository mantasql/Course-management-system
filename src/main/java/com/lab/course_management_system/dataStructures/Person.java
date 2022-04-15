package com.lab.course_management_system.dataStructures;

import java.io.Serializable;

public class Person extends User implements Serializable {

    private String name;
    private String surname;
    private String address;
    private String email;

    public Person(String login, String password, String name, String surname, String address, String email) {
        super(login, password);
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.email = email;
    }

    public Person(int id, String userName, String password, UserType userType) {
        super(id, userName, password, userType);
    }

    public Person(int id,String name, String surname) {
        super(id);
        this.name = name;
        this.surname = surname;
    }

    public Person(int id, UserType userType, String name, String surname, String address, String email) {
        super(id, userType);
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.email = email;
    }

    public Person(int id) {
        super(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

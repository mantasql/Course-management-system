package com.lab.course_management_system.dataStructures;

import java.io.Serializable;

public class Company extends User implements Serializable {

    private String companyName;
    private String address;
    private String phoneNumber;
    private String representative;

    public Company(String login, String password, String companyName, String address, String phoneNumber, String representative) {
        super(login, password);
        this.companyName = companyName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.representative = representative;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getRepresentative() {
        return representative;
    }
}

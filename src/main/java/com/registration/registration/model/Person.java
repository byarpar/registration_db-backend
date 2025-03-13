package com.registration.registration.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "people")
public class Person {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String nrc;
    private LocalDate dob;
    private String fatherName;
    private String phone;
    private String email;
    private String township;
    private String address;
    
    // Constructors
    public Person() {
    }
    
    public Person(String name, String nrc, LocalDate dob, String fatherName, 
                 String phone, String email, String township, String address) {
        this.name = name;
        this.nrc = nrc;
        this.dob = dob;
        this.fatherName = fatherName;
        this.phone = phone;
        this.email = email;
        this.township = township;
        this.address = address;
    }
    
    // Getters and Setters
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
    
    public String getNrc() {
        return nrc;
    }
    
    public void setNrc(String nrc) {
        this.nrc = nrc;
    }
    
    public LocalDate getDob() {
        return dob;
    }
    
    public void setDob(LocalDate dob) {
        this.dob = dob;
    }
    
    public String getFatherName() {
        return fatherName;
    }
    
    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getTownship() {
        return township;
    }
    
    public void setTownship(String township) {
        this.township = township;
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
                "id=" + id +
                ", name='" + name + '\'' +
                ", nrc='" + nrc + '\'' +
                ", dob=" + dob +
                ", fatherName='" + fatherName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", township='" + township + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
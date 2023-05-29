package com.example.csis3275groupproject.DB;

public class Employee {
    private String fullName;
    private String birthDate;
    private String email;
    private String password;
    private String phone;
    private String address;
    private String uid;

    private String jobTitle;
    private Long baseSalary;
    private String hireDate;
    private Boolean isAdmin;
    private String accountStatus;


    public Employee(String fName, String dOB, String emailAddress, String password, String phoneNum,
                    String address, String jobTitle, Long baseSalary, String hireDate, Boolean isAdmin, String uid) {
        this.fullName = fName;
        this.birthDate = dOB;
        this.email = emailAddress;
        this.password = password;
        this.phone = phoneNum;
        this.address = address;
        this.jobTitle = jobTitle;
        this.baseSalary = baseSalary;
        this.hireDate = hireDate;
        this.isAdmin = isAdmin;
        this.uid = uid;
    }

    public Employee(String fName, String dOB, String emailAddress, String password, String phoneNum, String address, String jobTitle, Long baseSalary, String hireDate, Boolean isAdmin, String uid, String accountStatus) {
        this.fullName = fName;
        this.birthDate = dOB;
        this.email = emailAddress;
        this.password = password;
        this.phone = phoneNum;
        this.address = address;
        this.jobTitle = jobTitle;
        this.baseSalary = baseSalary;
        this.hireDate = hireDate;
        this.isAdmin = isAdmin;
        this.uid = uid;
        this.accountStatus = accountStatus;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Long getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(Long baseSalary) {
        this.baseSalary = baseSalary;
    }

    public Employee() {
    }
    public Boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getHireDate() {
        return hireDate;
    }

    public void setHireDate(String hireDate) {
        this.hireDate = hireDate;
    }
}

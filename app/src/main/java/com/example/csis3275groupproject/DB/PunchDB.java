package com.example.csis3275groupproject.DB;

import java.text.SimpleDateFormat;

public class PunchDB {
    private String userID;
    private String email;
    private String rawDate;
    private String year;
    private String month;
    private String day;
    private String hour; //24 HOUR SYSTEM
    private String minute;
    private String second;
    private Boolean inOut;

    public PunchDB(String userID, String email, String rawDate, String year, String month,
                   String day, String hour, String minute, String second, Boolean inOut) {
        this.userID = userID;
        this.email = email;
        this.rawDate = rawDate;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.inOut = inOut;
    }
    public PunchDB(){

    }


    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRawDate() {
        return rawDate;
    }

    public void setRawDate(String rawDate) {
        this.rawDate = rawDate;
    }



    public Boolean getInOut() {
        return inOut;
    }

    public void setInOut(Boolean inOut) {
        this.inOut = inOut;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }
}

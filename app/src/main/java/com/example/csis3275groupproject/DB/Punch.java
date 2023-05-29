package com.example.csis3275groupproject.DB;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Punch {
    private String userID;
    private String email;
    private String rawDate;
    private String year;
    private String month;
    private String day;
    private String hour; //24 HOUR SYSTEM
    private String minute;
    private String second;
    private SimpleDateFormat dateFormat;
    private Boolean inOut; //for if they are punching in or out

    //punch object class that keeps track of everything relevant for the punches
    //again... sorry but currently all punches are treated as IN punches, still working on it

    public Punch(String userID, String rawDate, Boolean inOut, String email) throws ParseException {
        this.userID = userID;
        this.rawDate = rawDate;
        this.inOut = inOut;
        this.email = email;

        SimpleDateFormat initialFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
        Date currentDate = initialFormat.parse(rawDate);

        //setting specific parts of the time and date
        dateFormat = new SimpleDateFormat("yyyy");
        year = dateFormat.format(currentDate);
        dateFormat = new SimpleDateFormat("MM");
        month = dateFormat.format(currentDate);
        dateFormat = new SimpleDateFormat("dd");
        day = dateFormat.format(currentDate);
        dateFormat = new SimpleDateFormat("HH");
        hour = dateFormat.format(currentDate);
        dateFormat = new SimpleDateFormat("mm");
        minute = dateFormat.format(currentDate);
        dateFormat = new SimpleDateFormat("ss");
        second = dateFormat.format(currentDate);
    }



    public Punch(){
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getRawDate() {
        return rawDate;
    }

    public void setRawDate(String rawDate) {
        this.rawDate = rawDate;
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

    public Boolean getInOut() {
        return inOut;
    }

    public void setInOut(Boolean inOut) {
        this.inOut = inOut;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

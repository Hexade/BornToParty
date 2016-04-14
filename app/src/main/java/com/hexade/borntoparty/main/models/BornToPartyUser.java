package com.hexade.borntoparty.main.models;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by upendra on 3/13/16.
 */
public class BornToPartyUser extends GenericJson{
    // data store keys
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";

//    public static final String KEY_FIRST_NAME = "first_name";
//    public static final String KEY_LAST_NAME= "last_name";
//    public static final String KEY_EMAIL = "email";
//    public static final String KEY_DOB = "dob";

    @Key("_id")
    private String id;
    @Key
    public String gender;
    @Key
    public Name name;
    //    public String location;
    @Key
    public String email;
    @Key
    public String username;
//    public String password;
    @Key
    public String dob;
    @Key
    public String phone;
    @Key
    public String cell;
    @Key
    public Picture picture;

    public int daysLeft;
    public int age;
    public Date dateOfBirth;

    @Key
    public String[] friends;

    public BornToPartyUser(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Name getName() {
        return name == null ? new Name() : name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }

    public String getDob() {
        return dob;
    }

    public void setDob(String da) {
        Date dob = new Date();

        SimpleDateFormat sdfmt1 = new SimpleDateFormat("MMM dd, yyyy");
        try {
            dob = sdfmt1.parse(da);
        } catch (ParseException e) {
            e.printStackTrace();

        }

        Calendar d = Calendar.getInstance();
        d.setTime(dob);
        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.YEAR) - d.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) <= d.get(Calendar.DAY_OF_YEAR))
            age--;

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        d.set(Calendar.YEAR, year);

        if(d.before(cal))
            d.set(Calendar.YEAR, year + 1);

        // weird calculation. (Calendar.getTime()) gives Date. Date.getTime(); give long.
        long days = d.getTime().getTime() - cal.getTime().getTime();

        this.age = age;
        this.daysLeft = (int) TimeUnit.DAYS.convert(days, TimeUnit.MILLISECONDS);

        this.dateOfBirth = dob;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    public Picture getPicture() {
        return picture == null ? new Picture() : picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    public int getDaysLeft() {
        return daysLeft;
    }

    public int getAge() {
        return age;
    }

    public static Comparator<BornToPartyUser> getComparator(){
        return new Comparator<BornToPartyUser>() {
            @Override
            public int compare(BornToPartyUser lhs, BornToPartyUser rhs) {
                return lhs.getDaysLeft() - rhs.getDaysLeft();
            }
        };
    }

    public String getFormattedDob(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateOfBirth);
        return new SimpleDateFormat("MMMM d").format(cal.getTime());
    }

    public Date getDateOfBirth(){
        return dateOfBirth;
    }

    public String[] getFriends() {
        return friends;
    }

    public void setFriends(String[] friends) {
        this.friends = friends;
    }

    public static class Name extends GenericJson{
        @Key
        private String first;
        @Key
        private String last;
        @Key
        private String title;

        public Name(){

        }

        public String getFirst() {
            return first;
        }

        public void setFirst(String first) {
            this.first = first;
        }

        public String getLast() {
            return last;
        }

        public void setLast(String last) {
            this.last = last;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getFullName(){
            return first + " " + last;
        }
    }

    public static class Picture extends GenericJson{
        @Key
        public String large;
        @Key
        public String medium;
        @Key
        public String thumbnail;

        public Picture(){

        }

        public String getLarge() {
            return large;
        }

        public void setLarge(String large) {
            this.large = large;
        }

        public String getMedium() {
            return medium;
        }

        public void setMedium(String medium) {
            this.medium = medium;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }
    }
}

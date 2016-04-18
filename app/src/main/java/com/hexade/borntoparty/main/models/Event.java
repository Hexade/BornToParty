package com.hexade.borntoparty.main.models;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by himanshusoni on 4/13/16.
 */
public class Event extends GenericJson {

    @Key("_id")
    private String _id;
    @Key("name")
    private String eventName;
    @Key
    private String forUser;
    @Key
    private String createdBy;
    @Key
    private String discussionId;
    @Key
    private String location;
    @Key
    private ArrayList<String> invites;
    @Key
    private HashMap<String,String> inviteStatus;
    @Key
    private ArrayList<String> activities;
    @Key
    private String eventDate;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getForUser() {
        return forUser;
    }

    public void setForUser(String forUser) {
        this.forUser = forUser;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getDiscussionId() {
        return discussionId;
    }

    public void setDiscussionId(String discussionId) {
        this.discussionId = discussionId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ArrayList<String> getInvites() {
        return invites;
    }

    public void setInvites(ArrayList<String> invites) {
        this.invites = invites;
    }

    public HashMap<String, String> getInviteStatus() {
        return inviteStatus;
    }

    public void setInviteStatus(HashMap<String, String> inviteStatus) {
        this.inviteStatus = inviteStatus;
    }

    public ArrayList<String> getActivities() {
        return activities;
    }

    public void setActivities(ArrayList<String> activities) {
        this.activities = activities;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getFormattedDob(){
        Date dob = new Date();

        SimpleDateFormat sdfmt1 = new SimpleDateFormat("MMM dd, yyyy");
        try {
            dob = sdfmt1.parse(eventDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(dob);
        return new SimpleDateFormat("MMMM d").format(cal.getTime());
    }
}

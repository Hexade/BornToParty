package com.hexade.borntoparty.main.models;

import com.google.api.client.util.Key;
import com.kinvey.java.model.KinveyReference;

import java.util.ArrayList;

/**«
 * Created by himanshusoni on 4/14/16.
 */
public class EventUsers {

    @Key
    private String username;
    @Key("eventId")
    private ArrayList<Event> events;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

}

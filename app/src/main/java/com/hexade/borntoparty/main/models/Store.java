package com.hexade.borntoparty.main.models;

import java.util.ArrayList;

/**
 * Created by himanshusoni on 4/16/16.
 */
public class Store {
    public static ArrayList<BornToPartyUser> friendsList;
    public static ArrayList<Event> eventList;

    public static ArrayList<BornToPartyUser> getFriendsList() {
        return friendsList;
    }

    public static void setFriendsList(ArrayList<BornToPartyUser> friendsList) {
        Store.friendsList = friendsList;
    }

    public static ArrayList<Event> getEventList() {
        return eventList;
    }

    public static void setEventList(ArrayList<Event> eventList) {
        Store.eventList = eventList;
    }
}

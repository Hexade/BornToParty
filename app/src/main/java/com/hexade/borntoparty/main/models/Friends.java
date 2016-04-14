package com.hexade.borntoparty.main.models;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import com.kinvey.java.model.KinveyReference;

import java.util.ArrayList;

/**
 * Created by himanshusoni on 3/21/16.
 */
public class Friends extends GenericJson{

    @Key
    private String username;

    @Key
    private ArrayList<String> friends;

    private ArrayList<BornToPartyUser> friendsList;

    /*public void initReference(BornToPartyUser user){

        if(this.friends == null){
            this.friends = new ArrayList<KinveyReference>();
        }

        KinveyReference friends = new KinveyReference("usersmaster", user.get("username").toString());
        this.friends.add(friends);
    }*/

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<String> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<String> friends) {
        this.friends = friends;
    }

    public ArrayList<BornToPartyUser> getFriendsList() {
        return friendsList;
    }

    public void setFriendsList(ArrayList<BornToPartyUser> friendsList) {
        this.friendsList = friendsList;
    }
}

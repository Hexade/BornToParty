package com.hexade.borntoparty.main.UI.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hexade.borntoparty.main.R;
import com.hexade.borntoparty.main.models.BornToPartyUser;
import com.hexade.borntoparty.main.models.Event;
import com.kinvey.android.AsyncAppData;
import com.kinvey.java.core.KinveyClientCallback;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class CreateEventActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String ARG_ITEM_ID = "item_id";
    private BornToPartyUser mItem;
    private ArrayList<BornToPartyUser> myFriends;
    private boolean[] selectedFriends;
    private HashMap<String, String> invitedList = new HashMap<>();
    private HashSet<String> activitiesList = new HashSet<>();

    private Event newEvent;

    private TextView event_header;
    private TextView invitesText;
    private TextView activitiesText;

    private EditText event_location;

    private Button inviteFriends;
    private Button addActivities;
    private Button createEvent;

    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        event_header = (TextView) findViewById(R.id.event_header);
        invitesText = (TextView) findViewById(R.id.event_invites_info);
        activitiesText = (TextView) findViewById(R.id.event_activities_info);

        event_location = (EditText) findViewById(R.id.event_location);
        inviteFriends = (Button) findViewById(R.id.btn_invite);
        addActivities = (Button) findViewById(R.id.btn_add_activities);
        createEvent = (Button) findViewById(R.id.btn_create_event);

        inviteFriends.setOnClickListener(this);
        addActivities.setOnClickListener(this);
        createEvent.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();

        if (bundle.containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            String username = bundle.getString(ARG_ITEM_ID);

            ArrayList<BornToPartyUser> friendsList = MainActivity.myFriends.getFriendsList();
            for(BornToPartyUser f : friendsList){
                if(f.getUsername().equals(username)){
                    mItem = f;
                    break;
                }
            }

            myFriends = friendsList;
            selectedFriends = new boolean[myFriends.size() - 1];

            if(mItem!=null){
                event_header.setText(mItem.getName().getFirst() + "'s Birthday on " + mItem.getFormattedDob());
            }
        }

        pd = new ProgressDialog(this);
        pd.setTitle("Creating Awesomeness...");
        pd.setMessage("Please wait.");
        pd.setCancelable(false);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btn_invite :
                // display friends list and create a hashSet
                showInviteFriendsDialog();
                break;
            case R.id.btn_add_activities :
                // create a hashset from list of activities
                showAddActivitiesDialog();
                break;
            case R.id.btn_create_event :
                // save event in kinvey
                publishEvent();
                break;
        }
    }

    public void showInviteFriendsDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        CharSequence[] friendsNames = new CharSequence[myFriends.size() - 1];
        int j = 0;
        for(int i = 0; i < myFriends.size(); i++){
            if(myFriends.get(i) == mItem)
                continue;
            friendsNames[j++] = myFriends.get(i).getName().getFullName();
        }

        builder.setTitle("Select Friends")
                .setMultiChoiceItems(friendsNames, selectedFriends, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ListView lv = ((AlertDialog) dialog).getListView();
                        for(int i = 0; i < lv.getCount(); i++)
                        {
                            if(lv.isItemChecked(i)){
                                invitedList.put(myFriends.get(i).getUsername(), "Invite Sent");
                            } else {
                                invitedList.remove(myFriends.get(i).getUsername());
                            }
                        }

                        invitesText.setText(invitedList.size() + " friends invited");
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        builder.create().show();
    }

    public void showAddActivitiesDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final CharSequence[] listOfActivites = new CharSequence[]{
                "dance",
                "kick",
                "fly",
                "walk",
                "zumba",
                "Beer pong",
                "ping pong",
                "fuss ball"
        };

        builder.setTitle("Select Activities")
                .setMultiChoiceItems(listOfActivites, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ListView lv = ((AlertDialog) dialog).getListView();
                        for(int i = 0; i < lv.getCount(); i++)
                        {
                            if(lv.isItemChecked(i)){
                                activitiesList.add(listOfActivites[i].toString());
                            } else {
                                activitiesList.remove(listOfActivites[i]);
                            }
                        }

                        activitiesText.setText(activitiesList.size() + " activities planned");
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        builder.create().show();
    }

    public void publishEvent(){

        pd.show();

        // validate !
        invitedList.put(MainActivity.loggedInUser.getUsername(),"Coming");

        newEvent = new Event();
        newEvent.setCreatedBy(MainActivity.loggedInUser.getUsername());
        newEvent.setEventName(event_header.getText() + "");
        newEvent.setForUser(mItem.getUsername());
        newEvent.setLocation(event_location.getText() + "");
        newEvent.setActivities(new ArrayList<>(activitiesList));
        newEvent.setInviteStatus(invitedList);
        newEvent.setInvites(new ArrayList<>(invitedList.keySet()));
        newEvent.setEventDate(mItem.getDob());

        AsyncAppData<Event> myevents = MainActivity.kinveyClient.appData("events", Event.class);
        myevents.save(newEvent, new KinveyClientCallback<Event>() {
            @Override
            public void onFailure(Throwable e) {
                Log.e("TAG", "failed to save event data", e);

                // Stay on same page, display message
                pd.dismiss();
            }
            @Override
            public void onSuccess(Event r) {
                Log.d("TAG", "saved data for entity "+ r.getEventName());

                // show done display success and navigate back
                Toast.makeText(getApplicationContext(), "Party Created. Have a blast!", Toast.LENGTH_LONG).show();

                pd.dismiss();
                finish();
            }
        });
    }
}

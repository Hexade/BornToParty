package com.hexade.borntoparty.main.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hexade.borntoparty.main.R;
import com.hexade.borntoparty.main.UI.Fragments.ItemDetailFragment;
import com.hexade.borntoparty.main.models.BornToPartyUser;
import com.hexade.borntoparty.main.models.Event;
import com.hexade.borntoparty.main.models.Store;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class EventDetailActivity extends AppCompatActivity {

    public static final String ARG_ITEM_ID = "item_id";
    private Event mItem;

    private TextView event_header;
    private TextView invitesText;
    private TextView activitiesText;

    private TextView event_location;
    private TextView event_organizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        event_header = (TextView) findViewById(R.id.event_header);
        invitesText = (TextView) findViewById(R.id.event_invites_info);
        activitiesText = (TextView) findViewById(R.id.event_activities_info);
        event_location = (TextView) findViewById(R.id.event_location);
        event_organizer = (TextView) findViewById(R.id.event_organizer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.event_detail_toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                openDiscussion();
            }
        });


        Bundle bundle = getIntent().getExtras();

        if (bundle.containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            String eventId = bundle.getString(ARG_ITEM_ID);

            ArrayList<Event> eventList = Store.getEventList();
            for(Event f : eventList){
                if(f.get_id().equals(eventId)){
                    mItem = f;
                    break;
                }
            }

            if(mItem!=null){
                event_header.setText(mItem.getEventName());
                event_organizer.setText(mItem.getCreatedBy());
                event_location.setText(mItem.getLocation());
                invitesText.setText("Who's Coming\n"+mItem.getInvites().toString());
                activitiesText.setText("Activities\n"+mItem.getActivities().toString());
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void openDiscussion(){
        Intent intent = new Intent(this, DiscussionActivity.class);
        intent.putExtra(ARG_ITEM_ID, mItem.get_id());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}

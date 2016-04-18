package com.hexade.borntoparty.main.UI.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hexade.borntoparty.main.UI.Activities.CreateEventActivity;
import com.hexade.borntoparty.main.UI.Activities.ItemDetailActivity;
import com.hexade.borntoparty.main.UI.Activities.ItemListActivity;
import com.hexade.borntoparty.main.UI.Activities.MainActivity;
import com.hexade.borntoparty.main.R;
import com.hexade.borntoparty.main.Utils.DataManager;
import com.hexade.borntoparty.main.models.BornToPartyUser;
import com.hexade.borntoparty.main.models.Users;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class BirthdayDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private BornToPartyUser mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BirthdayDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            String username = getArguments().getString(ARG_ITEM_ID);

            ArrayList<BornToPartyUser> friendsList = MainActivity.myFriends.getFriendsList();
            for(BornToPartyUser f : friendsList){
                if(f.getUsername().equals(username)){
                    mItem = f;
                    break;
                }
            }


                    Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
//                appBarLayout.setTitle(mItem.content);
                ImageView backdrop = (ImageView)appBarLayout.findViewById(R.id.main_backdrop);
                Picasso.with(getActivity()).load(mItem.getPicture().getLarge()).skipMemoryCache().into(backdrop);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.birthday_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.item_detail)).setText(mItem.getName().getFullName());
            ((TextView) rootView.findViewById(R.id.item_date)).setText(mItem.getFormattedDob());
            ((TextView) rootView.findViewById(R.id.daysandage)).setText("Turns " + (mItem.getAge()+1) + " in "  + mItem.getDaysLeft() + " days!");
            ((TextView) rootView.findViewById(R.id.phone)).setText(mItem.getPhone()+"\n"+mItem.getCell()+"\n"+mItem.getEmail());

            Button btnPlanParty = (Button) rootView.findViewById(R.id.plan_party);
            Button btnSendWishes = (Button) rootView.findViewById(R.id.send_wishes);

            btnPlanParty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // TODO check if there is already a event created for this birthday
                    Intent intent = new Intent(getActivity(), CreateEventActivity.class);
                    intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, mItem.getUsername());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });
        }

        return rootView;
    }
}

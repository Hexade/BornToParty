package com.hexade.borntoparty.main.UI.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.api.client.json.GenericJson;
import com.hexade.borntoparty.main.MyBirthdayRecyclerViewAdapter;
import com.hexade.borntoparty.main.MyEventsRecyclerViewAdapter;
import com.hexade.borntoparty.main.R;
import com.hexade.borntoparty.main.UI.Activities.MainActivity;
import com.hexade.borntoparty.main.dummy.DummyEvent;
import com.hexade.borntoparty.main.dummy.DummyEvent.DummyItem;
import com.hexade.borntoparty.main.models.BornToPartyUser;
import com.hexade.borntoparty.main.models.Event;
import com.hexade.borntoparty.main.models.EventUsers;
import com.hexade.borntoparty.main.models.Friends;
import com.hexade.borntoparty.main.models.Store;
import com.kinvey.android.AsyncAppData;
import com.kinvey.android.AsyncCustomEndpoints;
import com.kinvey.android.AsyncUser;
import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyListCallback;
import com.kinvey.java.Query;
import com.kinvey.java.core.KinveyClientCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnEventsListFragmentInteractionListener}
 * interface.
 */
public class EventsFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnEventsListFragmentInteractionListener mListener;

    final Client mKinveyClient = MainActivity.kinveyClient;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EventsFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static EventsFragment newInstance(int columnCount) {
        EventsFragment fragment = new EventsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            final RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            //recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
//            recyclerView.setAdapter(new MyEventsRecyclerViewAdapter(DummyEvent.ITEMS, mListener));

            Query myQuery = mKinveyClient.query();

            myQuery.equals("username", MainActivity.loggedInUser.getUsername());//MainActivity.loggedInUser.getUsername());
            final AsyncAppData<EventUsers> myFriends = mKinveyClient.appData("eventusers", EventUsers.class);
            myFriends.get(myQuery, new KinveyListCallback<EventUsers>() {
                @Override
                public void onSuccess(EventUsers[] results) {
                    Log.v("TAG", "received " + results.length + " friends");

                    if(results.length > 0){
                        Log.i("RESULT",results[0].getUsername());
                        Log.i("RESULT",results[0].getEvents().toString());
                        ArrayList<Event> eventList = results[0].getEvents();
                        Store.eventList = eventList;

                        recyclerView.setAdapter(new MyEventsRecyclerViewAdapter(eventList, mListener));
                    }
                }

                @Override
                public void onFailure(Throwable error) {
                    Log.e("TAG", "failed to fetchByFilterCriteria 1", error);
                }
            });
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnEventsListFragmentInteractionListener) {
            mListener = (OnEventsListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnEventsListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnEventsListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onEventsListFragmentInteraction(Event item);
    }
}

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

import com.hexade.borntoparty.main.DividerItemDecoration;
import com.hexade.borntoparty.main.MyBirthdayRecyclerViewAdapter;
import com.hexade.borntoparty.main.R;
import com.hexade.borntoparty.main.UI.Activities.MainActivity;
import com.hexade.borntoparty.main.models.BornToPartyUser;
import com.hexade.borntoparty.main.models.Friends;
import com.hexade.borntoparty.main.models.Store;
import com.hexade.borntoparty.main.models.Users;
import com.kinvey.android.AsyncAppData;
import com.kinvey.android.AsyncUser;
import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyListCallback;
import com.kinvey.java.Query;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * A fragment representing a list of Items.
 * <p>
 * Activities containing this fragment MUST implement the {@link OnBirthdayListFragmentInteractionListener}
 * interface.
 */
public class BirthdayFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnBirthdayListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BirthdayFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static BirthdayFragment newInstance(int columnCount) {
        BirthdayFragment fragment = new BirthdayFragment();
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
        View view = inflater.inflate(R.layout.fragment_birthday_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            final RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
            Log.i("API", "In BirthdayFragment, onCreateView");

            final Client mKinveyClient = MainActivity.kinveyClient;

            final BornToPartyUser friends = new BornToPartyUser();
            Query myQuery = mKinveyClient.query();

            myQuery.equals("username", MainActivity.loggedInUser.getUsername());//MainActivity.loggedInUser.getUsername());
            final AsyncAppData<Friends> myFriends = mKinveyClient.appData("friends", Friends.class);
            myFriends.get(myQuery, new KinveyListCallback<Friends>() {
                @Override
                public void onSuccess(Friends[] results) {
                    Log.v("TAG", "received " + results.length + " friends");

                    if(results.length > 0){
                        Log.i("RESULT",results[0].getUsername());
                        Log.i("RESULT",results[0].getFriends().toString());
                        Log.i("RESULT",results[0].getFriends().size() + "");

                        Query query = new Query();
                        query.in("username", results[0].getFriends().toArray());//MainActivity.loggedInUser.getUsername());
                        AsyncAppData<BornToPartyUser> myFriendList = mKinveyClient.appData("usersmaster", BornToPartyUser.class);
                        myFriendList.get(query, new KinveyListCallback<BornToPartyUser>() {
                            @Override
                            public void onSuccess(BornToPartyUser[] results) {
                                Log.v("TAG", "received " + results.length + " friends");

                                if (results.length > 0) {
                                    Log.i("RESULT", " friends list " + results.length);
                                    Log.i("RESULT", " friends list " + results[0].getUsername());

                                    Friends myFriends = new Friends();

                                    MainActivity.myFriends = myFriends;

                                    AsyncUser user = mKinveyClient.user();
                                    myFriends.setUsername(user.getUsername());

                                    ArrayList<BornToPartyUser> friendList = new ArrayList<>(Arrays.asList(results));
                                    for(BornToPartyUser f : friendList){
                                        f.setDob(f.getDob());
                                    }

                                    if(friendList.size()!=0){
                                        Collections.sort(friendList, BornToPartyUser.getComparator());
                                    }

                                    myFriends.setFriendsList(friendList);
                                    Store.friendsList = friendList;

                                    recyclerView.setAdapter(new MyBirthdayRecyclerViewAdapter(getActivity(), friendList, mListener));
                                }
                            }

                            @Override
                            public void onFailure(Throwable error) {
                                Log.e("TAG", "failed to fetchByFilterCriteria 2", error);
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Throwable error) {
                    Log.e("TAG", "failed to fetchByFilterCriteria 1", error);
                }
            });

//            new Users().fetch();
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnBirthdayListFragmentInteractionListener) {
            mListener = (OnBirthdayListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnBirthdayListFragmentInteractionListener");
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnBirthdayListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onBirthdayListFragmentInteraction(BornToPartyUser item);
    }
}

package com.hexade.borntoparty.main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hexade.borntoparty.main.UI.Fragments.EventsFragment.OnEventsListFragmentInteractionListener;
import com.hexade.borntoparty.main.dummy.DummyEvent.DummyItem;
import com.hexade.borntoparty.main.models.Event;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Event} and makes a call to the
 * specified {@link OnEventsListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyEventsRecyclerViewAdapter extends RecyclerView.Adapter<MyEventsRecyclerViewAdapter.ViewHolder> {

    private final List<Event> mValues;
    private final OnEventsListFragmentInteractionListener mListener;

    public MyEventsRecyclerViewAdapter(List<Event> items, OnEventsListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_events, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getForUser());
        holder.mContentView.setText(mValues.get(position).getEventName());

        holder.location.setText(mValues.get(position).getLocation());

        HashMap<String,Integer> inviteCount = new HashMap<>();
        HashMap<String, String> inviteStatus = mValues.get(position).getInviteStatus();

        for(Map.Entry<String, String> entry : inviteStatus.entrySet()){

            String value = entry.getValue();
            Integer count = inviteCount.get(value);
            if (count == null)
                inviteCount.put(value, new Integer(1));
            else
                inviteCount.put(value, new Integer(count+1));
        }

        String countString = "";
        for(Map.Entry<String, Integer> e : inviteCount.entrySet()){
            countString+= e.getKey() + ": " + e.getValue() + "\n";
        }

        holder.friends_count.setText(countString);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onEventsListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final TextView location;
        public final TextView friends_count;

        public Event mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
            location = (TextView) view.findViewById(R.id.location);
            friends_count = (TextView) view.findViewById(R.id.friends_count);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}

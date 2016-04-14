package com.hexade.borntoparty.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hexade.borntoparty.main.UI.Fragments.BirthdayFragment.OnBirthdayListFragmentInteractionListener;
import com.hexade.borntoparty.main.models.BornToPartyUser;
import com.hexade.borntoparty.main.models.Users;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link BornToPartyUser} and makes a call to the
 * specified {@link OnBirthdayListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyBirthdayRecyclerViewAdapter extends RecyclerView.Adapter<MyBirthdayRecyclerViewAdapter.ViewHolder> {

    private final List<BornToPartyUser> mValues;
    private final OnBirthdayListFragmentInteractionListener mListener;
    private final Context mContext;

    public MyBirthdayRecyclerViewAdapter(Context context, List<BornToPartyUser> items, OnBirthdayListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_birthday, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
//        holder.mIdView.setText(mValues.get(position).id);

        Picasso.with(mContext).load(mValues.get(position).getPicture().getThumbnail()).skipMemoryCache().into(holder.mThumbnail);

        holder.mUsernameView.setText(mValues.get(position).getName().getFullName());
        holder.mBirthdayView.setText(mValues.get(position).getFormattedDob());
        holder.mDaysLeftView.setText(mValues.get(position).getDaysLeft()+ " days");
        holder.mNewAgeView.setText("Turns " + (mValues.get(position).getAge() + 1));

        Log.i("API", " Data : " + position + ", " + mValues.get(position).getDaysLeft() + " , " + (mValues.get(position).getAge() + 1) + ", " + mValues.get(position).getDob());
//      Picasso.with(mContext).load(mValues.get(position).getThumbnail()).transform(new ScaleToFitWidhtHeigthTransform(mRowHeight, true)).skipMemoryCache().into(holder.image);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onBirthdayListFragmentInteraction(holder.mItem);
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
        public final ImageView mThumbnail;
//        public final TextView mIdView;
        public final TextView mUsernameView;
        public final TextView mBirthdayView;
        public final TextView mDaysLeftView;
        public final TextView mNewAgeView;

        public BornToPartyUser mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
//            mIdView = (TextView) view.findViewById(R.id.id);
            mThumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            mUsernameView = (TextView) view.findViewById(R.id.username);
            mBirthdayView = (TextView) view.findViewById(R.id.birthday);


            mDaysLeftView = (TextView) view.findViewById(R.id.daysleft);
            mNewAgeView = (TextView) view.findViewById(R.id.age);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mUsernameView.getText() + " '" + mBirthdayView.getText()+ "'";
        }
    }
}

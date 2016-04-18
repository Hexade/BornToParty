package com.hexade.borntoparty.main.models;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hexade.borntoparty.main.UI.Activities.MainActivity;
import com.kinvey.android.AsyncAppData;
import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyListCallback;
import com.kinvey.java.AppData;
import com.kinvey.java.Query;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by himanshusoni on 2/27/16.
 */
public class Users {

    public static void getUserData(String username, UserCallback mListener){
        final UserCallback listener = mListener;
        Client kinveyClient= MainActivity.kinveyClient;

        Query myQuery = kinveyClient.query();
        myQuery.equals("username", username);
        AsyncAppData<BornToPartyUser> myUser = kinveyClient.appData("usersmaster", BornToPartyUser.class);
    //                        myUser.getEntity(user.getId(), new KinveyClientCallback<BornToPartyUser>() {
        myUser.get(myQuery, new KinveyListCallback<BornToPartyUser>() {
            @Override
            public void onSuccess(BornToPartyUser[] resultList) {
                BornToPartyUser result;
                Log.v("TAG", "received " + resultList.length);
                if(resultList.length > 0){
                    result = resultList[0];
                }else {
                    return;
                }

                listener.getUserDataCompleted(result);

            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });

    }

    public interface UserCallback {
        // TODO: Update argument type and name
        void getUserDataCompleted(BornToPartyUser user);
    }
}

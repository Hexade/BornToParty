package com.hexade.borntoparty.main.UI.Activities;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hexade.borntoparty.main.R;
import com.hexade.borntoparty.main.kinvey.ClientService;
import com.hexade.borntoparty.main.models.BornToPartyUser;
import com.hexade.borntoparty.main.models.Friends;
import com.kinvey.android.AsyncAppData;
import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyListCallback;
import com.kinvey.java.Query;
import com.kinvey.java.User;
import com.kinvey.java.core.KinveyCancellableCallback;
import com.kinvey.java.core.KinveyClientCallback;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AccountAuthenticatorActivity {

    // UI references.
    private EditText mUsernameET;
    private EditText mPasswordET;
    private Button mSignInBtn;
    private Button mRegisterBtn;

    private Client kinveyClient;

    private AccountManager accountManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // init UI references
        mUsernameET = (EditText) findViewById(R.id.et_username);
        mPasswordET= (EditText) findViewById(R.id.et_password);
        mSignInBtn = (Button) findViewById(R.id.btn_sign_in);
        mRegisterBtn = (Button) findViewById(R.id.btn_register);

        mUsernameET.setText("crazysnake682");
        mPasswordET.setText("btp");

        kinveyClient = ((ClientService) getApplication()).getKinveyService();

        accountManager = AccountManager.get(getApplicationContext());
    }

    public void registerNewAccount(View view) {
        Intent intent = new Intent(this, RegisterAccountActivity.class);
        startActivity(intent);
    }

    public void signIn(View view) {
        if (!validateFields())
            return;

        login();
    }

    private void login() {
        // logout current active user
        if (kinveyClient.user().isUserLoggedIn())
            kinveyClient.user().logout().execute();

        kinveyClient.user().login(getUsername(), mPasswordET.getText().toString(),
                new KinveyCancellableCallback<User>() {
                    @Override
                    public boolean isCancelled() {
                        return false;
                    }

                    @Override
                    public void onCancelled() {

                    }

                    @Override
                    public void onSuccess(User user) {
                        Toast.makeText(getApplicationContext(), "Logged in as " + user.getUsername(),
                                Toast.LENGTH_LONG).show();
                        authenticate(user.getId());

                        BornToPartyUser user1 = new BornToPartyUser();

                        // TODO issue with the id for usermaster. Need to be same as users. So using query instead of entity

                        Query myQuery = kinveyClient.query();
                        myQuery.equals("username", user.getUsername());
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

                                MainActivity.setLoggedInUser(result);
//                                SharedPreferences mPrefs = getSharedPreferences("btpPref", Context.MODE_PRIVATE);
                                SharedPreferences  mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                                SharedPreferences.Editor prefsEditor = mPrefs.edit();
//                                Gson gson = new Gson();
//                                String json = gson.toJson(result);
                                prefsEditor.putString("loggedInUser", result.getUsername());
                                prefsEditor.commit();


                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                LoginActivity.this.startActivity(intent);
                                LoginActivity.this.finish();
                            }

                            @Override
                            public void onFailure(Throwable throwable) {
                                Toast.makeText(getApplicationContext(), "Login failed: " + throwable.getMessage(),
                                        Toast.LENGTH_LONG).show();
                                authenticate(null);
                            }
                        });
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Toast.makeText(getApplicationContext(), "Login failed: " + throwable.getMessage(),
                                Toast.LENGTH_LONG).show();
                        authenticate(null);
                    }
                });
    }

    private void authenticate(String id) {
        if (id == null || id.isEmpty()) {
            return;
        }

        // add or update account in Android Account Manager
        final Account account = new Account(getUsername(), ClientService.ACCOUNT_TYPE);

        String currUsername = getIntent().getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
        if (currUsername == null) {
            accountManager.addAccountExplicitly(account, mPasswordET.getText().toString(), null);
        } else {
            accountManager.setPassword(account, mPasswordET.getText().toString());
        }

        final Intent intent = new Intent();
        intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, getUsername());
        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, ClientService.ACCOUNT_TYPE);
        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);
    }

    private boolean validateFields() {
        if (getUsername().isEmpty()) {
            showErrorCannotBeEmpty(mUsernameET.getHint().toString());
            return false;
        }
        if (mPasswordET.getText().toString().isEmpty()) {
            showErrorCannotBeEmpty(mPasswordET.getHint().toString());
            return false;
        }

        return true;
    }

    @NonNull
    private String getUsername() {
        return mUsernameET.getText().toString().trim();
    }

    private void showErrorCannotBeEmpty(String s) {
        showValidationError(s + " cannot be empty!");
    }

    private void showValidationError(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }
}


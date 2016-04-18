package com.hexade.borntoparty.main.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hexade.borntoparty.main.UI.Fragments.DatePickerFragment;
import com.hexade.borntoparty.main.R;
import com.hexade.borntoparty.main.kinvey.ClientService;
import com.hexade.borntoparty.main.models.BornToPartyUser;
import com.kinvey.android.AsyncAppData;
import com.kinvey.android.AsyncUser;
import com.kinvey.android.Client;
import com.kinvey.java.User;
import com.kinvey.java.core.KinveyCancellableCallback;
import com.kinvey.java.core.KinveyClientCallback;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RegisterAccountActivity extends FragmentActivity
                                    implements DatePickerFragment.OnDatePickerSetListener {

    // UI references
    private EditText mUsernameET;
    private EditText mFirstNameET;
    private EditText mLastNameET;
    private EditText mEmailET;
    private EditText mPasswordET;
    private EditText mConfirmPasswordET;
    private TextView mDateOfBirthTV;
    private Button mDateOfBirthBtn;
    private Button mRegisterBtn;

    private Client kinveyClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);

        // init UI references
        mUsernameET = (EditText) findViewById(R.id.et_reg_username);
        mFirstNameET= (EditText) findViewById(R.id.et_first_name);
        mLastNameET = (EditText) findViewById(R.id.et_last_name);
        mEmailET = (EditText) findViewById(R.id.et_reg_email);
        mPasswordET = (EditText) findViewById(R.id.et_reg_password);
        mConfirmPasswordET = (EditText) findViewById(R.id.et_reg_confirm_password);

        mDateOfBirthTV = (TextView) findViewById(R.id.tv_date_of_birth);
        mDateOfBirthBtn = (Button) findViewById(R.id.btn_pick_date_of_birth);
        mRegisterBtn = (Button) findViewById(R.id.btn_reg_register);

        // init kinvey client
        kinveyClient = ((ClientService)getApplication()).getKinveyService();
    }

    public void pickDateOfBirth(View view) {
        DialogFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(getSupportFragmentManager(),
                DatePickerFragment.TAG_DATE_PICKER);
    }

    public void registerAccount(View view) {
        if (!validateFields())
            return;

        // logout active user
        if (kinveyClient.user().isUserLoggedIn())
            kinveyClient.user().logout().execute();

        // create a user with unique field
        kinveyClient.user().create(mUsernameET.getText().toString().trim(),
                mPasswordET.getText().toString(), new KinveyCancellableCallback<User>() {
                    @Override
                    public boolean isCancelled() {
                        return false;
                    }

                    @Override
                    public void onCancelled() {

                    }

                    @Override
                    public void onSuccess(User user) {
                        AsyncUser asyncUser = RegisterAccountActivity.this.kinveyClient.user();
//                        asyncUser.put(BornToPartyUser.KEY_FIRST_NAME, mFirstNameET.getText().toString().trim());
//                        asyncUser.put(BornToPartyUser.KEY_LAST_NAME, mLastNameET.getText().toString().trim());
//                        asyncUser.put(BornToPartyUser.KEY_DOB, mDateOfBirthTV.getText().toString());
//                        asyncUser.put(BornToPartyUser.KEY_EMAIL, mEmailET.getText().toString().trim());

                        BornToPartyUser btpUser = new BornToPartyUser();

                        BornToPartyUser.Name btpName = btpUser.getName();
                        btpUser.setId(asyncUser.getId());

                        btpUser.setUsername(asyncUser.getUsername());
                        btpName.setFirst(mFirstNameET.getText().toString().trim());
                        btpName.setLast(mLastNameET.getText().toString().trim());
                        btpUser.setName(btpName);

                        btpUser.setDob(mDateOfBirthTV.getText().toString());
                        btpUser.setEmail(mEmailET.getText().toString().trim());

                        btpUser.setCell("05-343-596");
                        btpUser.setPhone("049-677-04-77");

                        BornToPartyUser.Picture btpPic = btpUser.getPicture();
                        btpPic.setLarge("https://randomuser.me/api/portraits/men/39.jpg");
                        btpPic.setThumbnail("https://randomuser.me/api/portraits/med/men/39.jpg");
                        btpPic.setMedium("https://randomuser.me/api/portraits/thumb/men/39.jpg");

                        btpUser.setPicture(btpPic);

                        AsyncAppData<BornToPartyUser> myevents = kinveyClient.appData("usersmaster", BornToPartyUser.class);
                        myevents.save(btpUser, new KinveyClientCallback<BornToPartyUser>() {
                            @Override
                            public void onFailure(Throwable e) {
                                Log.e("TAG", "failed to save event data", e);

                                showErrorRegistrationFailed(e.getMessage());
                            }
                            @Override
                            public void onSuccess(BornToPartyUser r) {
                                Log.d("TAG", "saved data for entity "+ r.getUsername());

                                // Todo Add row in friends

                                String info = "Registration successful: Please login to continue";
                                Toast.makeText(getApplicationContext(), info, Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(RegisterAccountActivity.this, LoginActivity.class);
                                RegisterAccountActivity.this.startActivity(intent);
                                RegisterAccountActivity.this.finish();
                            }
                        });

                        // update non unique fields for created user
                        /*asyncUser.update(new KinveyCancellableCallback<User>() {
                            @Override
                            public boolean isCancelled() {
                                return false;
                            }

                            @Override
                            public void onCancelled() {

                            }

                            @Override
                            public void onSuccess(User user) {
                                String info = "Registration successful: Please login to continue";
                                Toast.makeText(getApplicationContext(), info, Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(RegisterAccountActivity.this, LoginActivity.class);
                                RegisterAccountActivity.this.startActivity(intent);
                                RegisterAccountActivity.this.finish();
                            }

                            @Override
                            public void onFailure(Throwable throwable) {
                                showErrorRegistrationFailed(throwable.getMessage());
                            }
                        });*/
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        showErrorRegistrationFailed(throwable.getMessage());
                    }
                });
    }

    private void showErrorRegistrationFailed(String err) {
        String text = "User registration failed: " + err;
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    private boolean validateFields() {
        if (mUsernameET.getText().toString().trim().isEmpty()) {
            showErrorCannotBeEmpty(mUsernameET.getHint().toString());
            return false;
        }
        if (mFirstNameET.getText().toString().trim().isEmpty()) {
            showErrorCannotBeEmpty(mFirstNameET.getHint().toString());
            return false;
        }
        if (mLastNameET.getText().toString().trim().isEmpty()) {
            showErrorCannotBeEmpty(mLastNameET.getHint().toString());
            return false;
        }
        if (mEmailET.getText().toString().trim().isEmpty()) {
            showErrorCannotBeEmpty(mEmailET.getHint().toString());
            return false;
        }
        if (mPasswordET.getText().toString().isEmpty()) {
            showErrorCannotBeEmpty(mPasswordET.getHint().toString());
            return false;
        }
        if (!mConfirmPasswordET.getText().toString().equals(mPasswordET.getText().toString())){
            showValidationError("Passwords do not match!");
            return false;
        }

        return true;
    }

    private void showErrorCannotBeEmpty(String s) {
        showValidationError(s + " cannot be empty!");
    }

    private void showValidationError(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void OnDatePickerSet(int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy");
        mDateOfBirthTV.setText(sdf.format(c.getTime()));
    }
}

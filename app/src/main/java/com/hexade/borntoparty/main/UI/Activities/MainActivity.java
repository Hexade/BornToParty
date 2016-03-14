package com.hexade.borntoparty.main.UI.Activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.hexade.borntoparty.main.R;
import com.hexade.borntoparty.main.UI.Fragments.BirthdayDetailFragment;
import com.hexade.borntoparty.main.UI.Fragments.BirthdayFragment;
import com.hexade.borntoparty.main.UI.Fragments.EventsFragment;
import com.hexade.borntoparty.main.UI.Fragments.HomeFragment;
import com.hexade.borntoparty.main.UI.Fragments.InviteFragment;
import com.hexade.borntoparty.main.dummy.DummyContent;
import com.hexade.borntoparty.main.dummy.DummyEvent;
import com.hexade.borntoparty.main.kinvey.ClientService;
import com.hexade.borntoparty.main.models.BornToPartyUser;
import com.hexade.borntoparty.main.models.Users;
import com.kinvey.android.AsyncUser;
import com.kinvey.android.Client;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        BirthdayFragment.OnBirthdayListFragmentInteractionListener,
        EventsFragment.OnEventsListFragmentInteractionListener,
        InviteFragment.OnInviteListFragmentInteractionListener,
        HomeFragment.OnFragmentInteractionListener{

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private boolean isUserInfoSet;

    public static Context myAppContext;

    private Client kinveyClient;

    // UI references
    private NavigationView navigationView;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isUserInfoSet = false;

        // get kinveyClient
        this.kinveyClient = ((ClientService) getApplication()).getKinveyService();

        if (!isLoggedIn()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return;
        }

        myAppContext = getApplicationContext();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, new HomeFragment()).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if (slideOffset != 0)
                    setUserInfo();

                super.onDrawerSlide(drawerView, slideOffset);
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    // set username and full name in nav header;
    private void setUserInfo() {
        if (isUserInfoSet)
            return;

        AsyncUser user = kinveyClient.user();
        String firstName = (String) user.get(BornToPartyUser.KEY_FIRST_NAME);
        String lastName = (String) user.get(BornToPartyUser.KEY_LAST_NAME);

        View headerView = navigationView.getHeaderView(0);

        if (firstName != null && lastName !=null) {
            String userFullName = firstName.toString() + " " + lastName.toString();
            TextView fullNameTV = (TextView) headerView.findViewById(R.id.tv_nav_header_full_name);
            fullNameTV.setText(userFullName);
            isUserInfoSet = true;
        }

        if (user.getUsername() != null) {
            String username = "@" + user.getUsername();
            TextView usernameTV = (TextView) headerView.findViewById(R.id.tv_nav_header_username);
            usernameTV.setText(username);
        }
    }

    private boolean isLoggedIn() {
        AccountManager am = AccountManager.get(getApplicationContext());
        Account[] accounts = am.getAccountsByType(ClientService.ACCOUNT_TYPE);

        return accounts.length > 0 && kinveyClient.user().isUserLoggedIn();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            // open a modal to enter the username to add new Friend;
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Log.i("NAV-CLICK", "item " + item.getTitle());
        selectDrawerItem(item);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void selectDrawerItem(MenuItem item){
        // Create a new fragment and specify the planet to show based on
        // position
        Fragment fragment = null;

        Class fragmentClass;
        switch(item.getItemId()) {
            case R.id.nav_home:
                fragmentClass = HomeFragment.class;
                break;
            /*case R.id.nav_events:
                fragmentClass = EventsFragment.class;
                break;*/
            case R.id.nav_invites:
                fragmentClass = InviteFragment.class;
                break;
            /*case R.id.nav_friends:
                // TODO open a new Activity
                break;
            case R.id.nav_settings:
                // TODO open a new Activity
                break;*/
            case R.id.nav_logout:
                logOut();
            default:
                fragmentClass = HomeFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        // Highlight the selected item, update the title, and close the drawer
        item.setChecked(true);
        setTitle(item.getTitle());

    }

    @Override
    public void onBirthdayListFragmentInteraction(Users.User item) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putString(BirthdayDetailFragment.ARG_ITEM_ID, item.getUsername());
            BirthdayDetailFragment fragment = new BirthdayDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit();
        } else {
            Context context = getApplicationContext();
            Intent intent = new Intent(context, BirthdayDetailActivity.class);
            intent.putExtra(BirthdayDetailFragment.ARG_ITEM_ID, item.getUsername());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(intent);
        }
    }

    @Override
    public void onEventsListFragmentInteraction(DummyEvent.DummyItem item) {
        /*if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putString(ItemDetailFragment.ARG_ITEM_ID, item.id);
            ItemDetailFragment fragment = new ItemDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit();
        } else {*/
            Context context = getApplicationContext();
            Intent intent = new Intent(context, EventDetailActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(intent);
//        }

    }

    @Override
    public void onInviteListFragmentInteraction(DummyContent.DummyItem item) {
        DialogFragment reminderDialogFragment = new DialogFragment() {
            public String[] inviteActions =  new String[]{"Yes","No","May be","Later"};

            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Send a Response")
                        .setItems(inviteActions, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // The 'which' argument contains the index position
                                // of the selected item
                                Log.i("INVITE", inviteActions[which]);

                                switch (which){
                                    case 0:
                                        break;
                                    case 1:
                                        break;
                                    case 2:
                                        break;
                                    default:
                                }
                            }
                        });
                return builder.create();
            }
        };

        reminderDialogFragment.show(getSupportFragmentManager(), "RemindersFragment");
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void logOut() {
        /*
         * TODO: Log out is not an essentaial feature. Can be removed later
         * Keeping this here as a debugging and testing purposes
         * Need a better implementation to push this feature to production
         */
        kinveyClient.user().logout().execute();

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);

        /*
        // remove account from AccountManager
        AccountManager am = AccountManager.get(getApplicationContext());
        Account[] accounts = am.getAccountsByType(ClientService.ACCOUNT_TYPE);
        if (accounts.length > 0)
            am.removeAccount(accounts[0], null, new AccountManagerCallback<Bundle>() {
                @Override
                public void run(AccountManagerFuture<Bundle> future) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }, null);
        */
    }

/*    private void setupViewPager(ViewPager viewPager) {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(BirthdayFragment.newInstance(0), "Birthdays");
        adapter.addFragment(EventsFragment.newInstance(1), "Events");

        viewPager.setAdapter(adapter);
    }

    *//**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     *//*
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }*/
}

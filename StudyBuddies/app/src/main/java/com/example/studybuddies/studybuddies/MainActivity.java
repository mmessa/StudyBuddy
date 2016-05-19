package com.example.studybuddies.studybuddies;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.transition.AutoTransition;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Logger;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import dao.Course;
import dao.DaoService;
import dao.User;

public class MainActivity extends AppCompatActivity
        implements  NavigationView.OnNavigationItemSelectedListener,
                    CourseFragment.OnFragmentInteractionListener,
                    GroupFragment.OnFragmentInteractionListener,
                    ProfileFragment.OnFragmentInteractionListener,
                    GoogleApiClient.OnConnectionFailedListener,
                    View.OnClickListener,
                    GoogleApiClient.ConnectionCallbacks {

    private static final String TAG = "MainActivity";

    public static DaoService daoService = new DaoService();
    public static GoogleApiClient mGoogleApiClient;

    public Location mLastLocation;

    private ProgressDialog mProgressDialog;

    private static final int RC_SIGN_IN = 9001;

    private static final int REQUEST_LOCATION = 0;


    public static String userEmail;
    public static String userName;
    public static String userId;
    public static List courseList = new ArrayList();
    public static List groupList = new ArrayList();
    public static List userList = new ArrayList();

    public static LatLng userLatLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        findViewById(R.id.sign_in_button).setOnClickListener(this);

        // Firebase connection

        Firebase.setAndroidContext(this);
        //Firebase.getDefaultConfig().setLogLevel(Logger.Level.DEBUG);
        Firebase myFirebaseRef = new Firebase("https://vivid-heat-5794.firebaseio.com/");
        //myFirebaseRef.child("message").child("submessage").setValue("This is text!");


        // Google Api Client and Google Sign-in
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .addApi(LocationServices.API)
                .build();

        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setScopes(gso.getScopeArray());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // NAV DRAWER CODE
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        // END NAV DRAWER CODE

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        //sets up listeners to maintain global lists(courses, groups) and
        // the course & group next ids
        daoService.addNextCourseNumberListener();
        daoService.addNextGroupNumberListener();
        daoService.addCourseListListener();
        daoService.addGroupListListener();
        daoService.addUserListListener();


        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if(opr.isDone()) {
            // checks if users cached credentials are valid
            Log.d(TAG, "Cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        }
        else {
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
        mGoogleApiClient.connect();
    }

    private void hideProgressDialog() {
        if(mProgressDialog != null && mProgressDialog.isShowing()){
            mProgressDialog.hide();
        }
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Attempting to sign in using Google");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        Fragment fragment = null;
        Class fragmentClass = null;
        if (id == R.id.nav_profile) {
            fragmentClass = ProfileFragment.class;
            System.out.println("calling create group");

            daoService.createCourse("Parallel", 551);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            daoService.createGroup("James' Group", 0);

            System.out.printf("%d", MainActivity.courseList.size());
            System.out.println(MainActivity.courseList);


        } else if (id == R.id.nav_courses) {
            fragmentClass = CourseFragment.class;
            //daoService.joinCourse(0);


        } else if (id == R.id.nav_groups) {
            fragmentClass = GroupFragment.class;
            //daoService.joinGroup(0);
        }  //else if (id == R.id.nav_share) {
        //} else if (id == R.id.nav_send) {
        //}

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e ) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.scene_root,fragment).commit();
        item.setChecked(true);
        setTitle(item.getTitle());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onClick(View v) {
       switch (v.getId()) {
           case R.id.sign_in_button:
               signIn();
               break;
       }

    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());

        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            Log.d(TAG, "acct info:" + acct.getDisplayName());
            Log.d(TAG, "acct info:" + acct.getEmail());
            Log.d(TAG, "acct info:" + acct.getId());


            userEmail = acct.getEmail();
            userName = acct.getDisplayName();
            userId = acct.getId();

            Firebase ref = new Firebase("https://vivid-heat-5794.firebaseio.com/User");
            final Query queryRef = ref.orderByChild("email").equalTo(userEmail);

            queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        System.out.printf("snapshot not null\n");
                    } else {
                        System.out.printf("snapshot is null\n");
                        User user = new User();
                        user.setUserId(userId);
                        user.setName(userName);
                        user.setEmail(userEmail);

                        Firebase myFirebaseRef = new Firebase("https://vivid-heat-5794.firebaseio.com/");
                        Firebase userRef = myFirebaseRef.child("User").child(userId);

                        userRef.setValue(user);

                    }


                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                }
            });


            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }

    private void updateUI(boolean signedIn) {
        if (signedIn) {
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
        } else {
            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);

        }
    }


    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(mLastLocation != null) {
            Log.d(TAG, "Latitude: " + mLastLocation.getLatitude());
            Log.d(TAG, "Longitude: " + mLastLocation.getLongitude());
        } else {
            Log.d(TAG, "No location detected");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public void getLocation(View view) {

        Log.d(TAG,"Get location button clicked. Checking Permision.");

        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ){
            requestLocationPermission();
        } else {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if(mLastLocation != null) {
                Log.d(TAG, "Latitude: " + mLastLocation.getLatitude());
                Log.d(TAG, "Longitude: " + mLastLocation.getLongitude());
                userLatLng = new LatLng(mLastLocation.getLatitude(),
                                        mLastLocation.getLongitude());
            } else {
                Log.d(TAG, "No location detected");
            }
        }

    }

    private void requestLocationPermission() {
        Log.d(TAG, "Requesting Location Permision");

        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_LOCATION);

    }

}

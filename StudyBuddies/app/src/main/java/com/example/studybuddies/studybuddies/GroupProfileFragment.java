package com.example.studybuddies.studybuddies;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.firebase.client.Firebase;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.jar.Manifest;

import dao.Group;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GroupProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GroupProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupProfileFragment extends Fragment
        implements
        OnMapReadyCallback,
        OnMyLocationButtonClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String TAG = "GroupProfileFragment";

    // TODO: Rename and change types of parameters
    private String mParam1;

    private String mParam2;

    private GoogleMap mMap;

    private LocationRequest mLocationRequest;

    private GoogleApiClient mGoogleApiClient;

    private Location mLastLocation;

    private Group mCurrentGroup;

    private HashMap<String,double[]> mCurrentLocations;

    private List<double[]> mCurrentLocationsList;

    private Marker mCurrLocationMarker;

    private static final LatLng CHICO = new LatLng(39.7285, -121.8375);

    private OnFragmentInteractionListener mListener;

    private static final int REQUEST_LOCATION = 0;

    public GroupProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GroupProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GroupProfileFragment newInstance(String param1, String param2) {
        GroupProfileFragment fragment = new GroupProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        //SupportMapFragment mapFragment = ((SupportMapFragment) getFragmentManager()
        //        .findFragmentById(R.id.map));
        //mapFragment.getMapAsync(this);
    }

    public void onMapReady(GoogleMap map) {
        mMap = map;

        if (MainActivity.userLatLng != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(MainActivity.userLatLng));
        } else {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(CHICO));
        }
        mMap.setOnMyLocationButtonClickListener(this);
        Log.d(TAG, "onMapReady: " + Integer.toString(Build.VERSION.SDK_INT));
        Log.d(TAG, "onMapReady: " + Integer.toString(Build.VERSION_CODES.M));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "onMapReady: Location Permission Granted");
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            } else {
                Log.d(TAG, "onMapReady: Location Permission Not Granted");
                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MainActivity.REQUEST_LOCATION);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        if(mCurrentLocations != null) {

            Iterator it = mCurrentLocations.entrySet().iterator();
            while (it.hasNext()) {
                HashMap.Entry pair = (HashMap.Entry)it.next();
                //Log.d(TAG, "onMapReady: "+(HashMap.Entry)it.);
                double[] test = (double[])pair.getValue();
                LatLng latlng = new LatLng(test[0],test[1]);
                mMap.addMarker(new MarkerOptions()
                .position(latlng)
                .title((String) pair.getKey()));
            }
        }

        Log.d(TAG, "onMapReady: Past Permissions");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_group_profile, container, false);

        //Getting the infromation from the previous fragment
        Bundle bundle = getArguments();

        //Getting the group from the id
        final int group_id_passed_in = bundle.getInt("group_id_to_pass");
        mCurrentGroup = MainActivity.daoService.getGroup(group_id_passed_in);

        //Getting Current Locations in the group
        mCurrentLocations = mCurrentGroup.getUserLatLngs();
        if(mCurrentLocations == null) {
            mCurrentLocations = new HashMap<String, double[]>();
        }

        //Setting the Title
        (getActivity()).setTitle(mCurrentGroup.getName());


        //Checking the current sdk version for permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "onCreateView: permission granted");
            } else {
                Log.d(TAG, "onCreateView: permission denied");
                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MainActivity.REQUEST_LOCATION);
            }
        }

        //Getting the map from the view
        SupportMapFragment mapFragment = ((SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map));
        mapFragment.getMapAsync(this);

        //Getting the broadcast button and setting its onclick listener
        Button location_broadcaster = (Button) view.findViewById(R.id.location_broadcaster);
        location_broadcaster.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Firebase ref = new Firebase("https://vivid-heat-5794.firebaseio.com/")
                        .child("Group")
                        .child(Integer.toString(mCurrentGroup.getGroupId()));
                double[] latlng = {mLastLocation.getLatitude(), mLastLocation.getLongitude()};

                Log.d(TAG, "onClick: "+Double.toString(latlng[0]));
                Log.d(TAG, "onClick: "+Double.toString(latlng[1]));
                double[] myLocationInGroup = new double[0];
                if(mCurrentLocations != null) {
                    myLocationInGroup = mCurrentLocations.get(MainActivity.userId);
                }
                if(myLocationInGroup == null) {
                    mCurrentLocations.put(MainActivity.userId, latlng);
                    mCurrentGroup.setUserLatLngs(mCurrentLocations);
                    ref.setValue(mCurrentGroup);
                } else {
                    mCurrentLocations.remove(MainActivity.userId);
                    mCurrentGroup.setUserLatLngs(mCurrentLocations);
                    ref.setValue(mCurrentGroup);
                }
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void setUpMapIfNeeded() {
        if (mMap != null) {
            return;
        }
        mMap = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        if (mMap == null) {
            return;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MainActivity.REQUEST_LOCATION) {
            if (permissions.length == 1 &&
                    permissions[0] == android.Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(getActivity(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    if(mGoogleApiClient == null){
                        buildGoogleApiClient();
                    }
                    mMap.setMyLocationEnabled(true);
                }
            }
        } else {
            Log.d(TAG, "onRequestPermissionsResult: false");
            mMap.setMyLocationEnabled(false);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }
}

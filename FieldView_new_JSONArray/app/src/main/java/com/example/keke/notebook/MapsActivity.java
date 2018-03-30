package com.example.keke.notebook;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener{



    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;

    //A request object to store parameters for requests to the FusedLocationProviderApi
    private LocationRequest mLocationRequest;

    //location where the device is currently located
    private Location mCurrentLocation;

    //marker for current Location
    private Marker mCurrentLocationMarker;

    private TextView _response;
    private Button GETbtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        _response = findViewById(R.id._response);


        GETbtn = findViewById(R.id.GETbtn);
        GETbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Instantiate the RequestQueue.
                RequestQueue queue = Volley.newRequestQueue(MapsActivity.this);
                // The URL where you want to send the request
                String url = "http://10.201.150.84:5000/fieldview/getAllJson";

                // Initialize a new JsonArrayRequest instance
                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                        Request.Method.GET, url, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {

                                //Process the JSON
                                try {

                                    //Loop through the array elements
                                    for (int i = 0; i < response.length(); i++) {
                                        //Get current json object
                                        JSONObject location = response.getJSONObject(i);

                                        //Get the current json object data
                                        String longitude = location.getString("longitude");
                                        String latitude = location.getString("latitude");

                                        // Display the formatted json in text view
                                        _response.append(longitude + " " + latitude + " ");
                                        _response.append("\n\n");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                _response.setText("It didn't work");
                            }
                        }
                );
                // Add JsonArrayRequest to the RequestQueue
                            queue.add(jsonArrayRequest);


                /*
                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the response string.
                                _response.setText(response);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        _response.setText("That didn't work!");
                    }
                }) {
                    //adding parameters to the request
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        return params;
                    }
                };
                // Add the request to the RequestQueue;
                queue.add(stringRequest);

                */
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap.clear();
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);


        mMap.addMarker(new MarkerOptions()
            .position(new LatLng(40.0057500000,  -105.2679000000))
                .title("Hello World!"));

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            //enables location layer
            mMap.setMyLocationEnabled(true);
        }
    }


    private synchronized void buildGoogleApiClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        //set the desired interval for active location updates, milliseconds
        mLocationRequest.setFastestInterval(1000);
        //set the fastest interval for location updates, milliseconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        //set priority of the request

        //check permission
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                    mLocationRequest, this);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[]grantResults){
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is canceled the result arrays are empty
                if(grantResults.length>0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {

                    //Permission was granted
                    if(ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)==
                            PackageManager.PERMISSION_GRANTED){
                        if(mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                        Toast.makeText(this, "permission granted", Toast.LENGTH_LONG).show();
                    }

                }else{
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        //check to see if there's a current marker
        if(mCurrentLocationMarker == null){
            //define an object of the Google MarkerOptions class
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Position");

            //place current location marker
            mCurrentLocationMarker = mMap.addMarker(markerOptions);
        }else {
            //set position of existing marker
            mCurrentLocationMarker.setPosition(latLng);
        }
        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
    }
    }

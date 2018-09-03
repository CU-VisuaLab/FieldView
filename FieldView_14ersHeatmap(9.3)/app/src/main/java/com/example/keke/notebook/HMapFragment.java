package com.example.keke.notebook;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class HMapFragment extends FragmentActivity implements OnMapReadyCallback {

    /**
     * Global Values for use in the app
     **/

    // Get the user's location for initializing the map camera
    private GoogleMap mMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heat_map);
        setUpMap();
    }

    @Override
    public void onResume(){
        super.onResume();
        setUpMap();
    }

    @Override
    public void onMapReady(GoogleMap map){
        if(mMap != null){
            return;
        }
        mMap = map;
        startDemo();
    }

    private void setUpMap(){
        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googlemap)) .getMapAsync(this);
    }

    // Run the Demo-specific code.


    protected abstract void startDemo();

    protected GoogleMap getMap() {
        return mMap;
    }

}
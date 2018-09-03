package com.example.keke.notebook;

import android.content.Intent;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HeatMapActivity extends HMapFragment {

    private HeatmapTileProvider mProvider;
    private TileOverlay mOverlay;
    ArrayList<LatLng> list = new ArrayList<LatLng>();


    // Add heatMap
    private void addHeatMap() {

        /** Pass the JsonArray from MobileVizActivity
            Get the data
         **/

        Intent intent = getIntent();
        String jsonArray = intent.getStringExtra("jsonArray");
        try {
            JSONArray array = new JSONArray(jsonArray);
            list = readItems(array);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        /**
        // Get the data: Lat/Lng positions of police stations
        try {
            Array = readItems(Array);
        } catch (JSONException e){
            Toast.makeText(this, "Problem reading list of locations.", Toast.LENGTH_LONG).show();
        }
        **/



        // Create a heat map tile provider, passing it the latlngs of colorado mountains.
        // step 1
        mProvider = new HeatmapTileProvider.Builder()
               .data(list)
               .build();
        // Add a title overlay to the map, using the heat map tile provider.
        //step 2 & 3
        // instead of mGoogleMap use getMap()
        mOverlay = getMap().addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
    }


    private ArrayList<LatLng> readItems(JSONArray array) throws JSONException {
        ArrayList<LatLng> list = new ArrayList<LatLng>();

        // Loop through the JsonArray
        for(int i = 0; i < array.length(); i++){
            JSONObject object = array.getJSONObject(i);
            //change to latitude and longitude for your data
            double lat = object.getDouble("latitude");
            double lng = object.getDouble("longitude");
            list.add(new LatLng(lat, lng));
        }
        return list;
    }


    @Override
    protected void startDemo() {
        getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(39.7392, -104.9903), 5));
        addHeatMap();

    }

}

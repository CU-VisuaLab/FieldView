package com.example.keke.notebook;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MobileVizActivity extends AppCompatActivity {

    private static final String TAG = "MobileVizActivity";

    // json object response url
    private String urlJsonArray = "http://cu-visualab.org/Fieldview/fieldview_mysql.php?password=database1";

    private Button btnMakeArrayRequest;

    //Define mobileViz imgbuttons
    private ImageButton barchartBtn;
    private ImageButton heatMapBtn;

    //Progress dialog
    private ProgressDialog progressDialog;


    //Temporary string to show the parsed response
    private String jsonResponse;


    // Create a bundle to handle data sharing
    Bundle bundle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_viz);

        btnMakeArrayRequest = (Button) findViewById(R.id.btnArrayRequest);


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);



        btnMakeArrayRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //making json array request
                makeJsonArrayRequest();
            }
        });

        //MobileViz btns
        barchartBtn = findViewById(R.id.barChart);
        barchartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBarchart();
            }
        });

        heatMapBtn = findViewById(R.id.heatMap);
        heatMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              openHeatMap();
            }
        });

    }
    /**
     *  Method to make Json array request where response starts with [
     */
    private void makeJsonArrayRequest() {
        showDialog();

        JsonArrayRequest req = new JsonArrayRequest(urlJsonArray,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {

                            // Initialize ArrayLists for peak & elevation storage
                            ArrayList<String> mdataList = new ArrayList<String>();
                            ArrayList<Integer> elevationList = new ArrayList<Integer>();

                            //Parsing json array response
                            //Loop through each json object
                            jsonResponse = "";
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject mountain = (JSONObject) response.get(i);
                                String peak = mountain.getString("peak");
                                String longitude = mountain.getString("longitude");
                                String latitude = mountain.getString("latitude");
                                String elevation = mountain.getString("elevation");


                                jsonResponse += "Peak: " + peak + "\n\n";
                                jsonResponse += "Longitude: " + longitude + "\n\n";
                                jsonResponse += "Latitude: " + latitude + "n\n";
                                jsonResponse += "Elevation: " + elevation + "\n\n\n";

                                // Add peak strings to the ArrayList
                                mdataList.add(peak);


                                // Convert elevation strings to integers
                                int elevations = Integer.parseInt(elevation);
                                // Add elevation integers to the ArrayList
                                elevationList.add(elevations);
                                // Add data to the bundle
                                bundle.putIntegerArrayList("elevationlist",elevationList);

                               // Pass the jsonArray
                                Intent intent = new Intent(MobileVizActivity.this, HeatMapActivity.class);
                                intent.putExtra("jsonArray", response.toString());
                                startActivity(intent);


                            }

                            // Populate data into a ListView
                            ListView listView = (ListView)findViewById(R.id.dataList);

                            // Display data in the fragment
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.mountain_list, mdataList);
                            listView.setAdapter(arrayAdapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                        hideDialog();
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                hideDialog();
            }
        });

        //Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);
    }

    // Open Charts Functions

    public void openBarchart() {
        Intent intent = new Intent(this, BarChartActivity.class);
        // Add the bundle to the intent
        intent.putExtras(bundle);
        startActivity(intent);
    }


    public void openHeatMap() {
        Intent intent = new Intent(this, HeatMapActivity.class);
        startActivity(intent);
    }



    private void showDialog() {
        if(!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }
}


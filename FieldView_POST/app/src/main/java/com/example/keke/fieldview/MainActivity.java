package com.example.keke.fieldview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    TextView _uid, _title, _description, _longitude, _latitude, _altitude, _created,  _response;
    android.support.v7.widget.AppCompatButton _sendRequest;
    ProgressBar _proProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Hooking the UI views for usage
        _uid =  findViewById(R.id.uid);
        _title = findViewById(R.id.title);
        _description =  findViewById(R.id.description);
        _longitude =  findViewById(R.id.longitude);
        _latitude =  findViewById(R.id.latitude);
        _altitude = findViewById(R.id.altitude);
        _created =  findViewById(R.id.created);
        _response =  findViewById(R.id.response);
        _proProgressBar =  findViewById(R.id.progressBar);
        _sendRequest =  findViewById(R.id.send_request);

        //hooking onclick listener of button
        _sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                // Instantiate the RequestQueue.
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                //this is the url where you want to send the request
                String url = "http://10.234.88.22:8000/entry/";
                Map<String, String> params = new HashMap<>();
                params.put("uid", _uid.getText().toString());
                params.put("title", _title.getText().toString());
                params.put("description", _description.getText().toString());
                params.put("longitude", _longitude.getText().toString());
                params.put("latitude", _latitude.getText().toString());
                params.put("altitude", _altitude.getText().toString());
                String time_stamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S", Locale.US).format(Calendar.getInstance().getTime());
                params.put("created", time_stamp);

                JsonObjectRequest stringRequest = new JsonObjectRequest(url, new JSONObject(params),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                _response.setText(response.toString());
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        _response.setText("That didn't work!");
                    }
                });
                // Add the request to the RequestQueue.
                queue.add(stringRequest);
            }
        });

    }
}
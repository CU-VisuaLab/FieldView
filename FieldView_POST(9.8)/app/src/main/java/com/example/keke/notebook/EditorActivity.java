package com.example.keke.notebook;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EditorActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    //Define a request code to send to Google Play services
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude;
    private double currentLongitude;


    private ImageButton takePictureBtn;
    private Button localSyncBtn;
    private Button setLocationBtn;


    // General code to post data
    String ServerURL = "http://cu-visualab.org/Fieldview/get_data.php";
    Button postBtn;
    EditText elevation, editor, lati, longi;
    String TempPeak, TempElevation, TempLat, TempLong;


    TextView Longitude;
    TextView Latitude;
    TextView ImageName;
    String action;
    String noteFilter;
    String oldText;

    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private static String TAG = "AddNewData";

    private static final int ERROR_DIALOG_REQUEST = 9001;
    private static String selectAllResponse = "";

    private String mCurrentPhotoPath;
    private Bitmap bm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);


        takePictureBtn = findViewById(R.id.takePicture);
        takePictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });

        localSyncBtn = findViewById(R.id.localSync);
        localSyncBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                syncLocal();
            }
        });

        setLocationBtn = findViewById(R.id.setLocation);


        // Code to handle data posting

        elevation = (EditText)findViewById(R.id.elevationEditText);
        editor = (EditText) findViewById(R.id.editText);
        lati = (EditText) findViewById(R.id.latEditext);
        longi = (EditText) findViewById(R.id.longEditext);
        postBtn = (Button) findViewById(R.id.postData);

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetInput();
                InsertData(TempPeak, TempElevation,TempLong, TempLat);
            }
        });

        // Lat & Long & Image textViews
        Longitude = findViewById(R.id.Longitude);
        Latitude = findViewById(R.id.Latitude);
        ImageName = findViewById(R.id.ImageName);

        // Code to handle data getting
        Button getBtn = (Button)findViewById(R.id.getData);
        getBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                pullData();
            }
        });


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                // The next two lines tell the new client that “this” current class will handle connection stuff
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                //fourth line adds the LocationServices API endpoint from GooglePlayServices
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1000); // 1 second, in milliseconds


        if (isServicesOK()) {
            init();
        }


        Intent intent = getIntent();

        Uri uri = intent.getParcelableExtra(NotesProvider.CONTENT_ITEM_TYPE);

        mDisplayDate = (TextView) findViewById(R.id.tv_add_date);
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(EditorActivity.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;

                Log.d(TAG, "onDateSet: date: mm/dd/yyy: " + month + "/" + day + "/" + year);
                String date = month + "/" + day + "/" + year;
                mDisplayDate.setText(date);
            }
        };

        if (uri == null) {
            action = Intent.ACTION_INSERT;
            setTitle(getString(R.string.new_note));
        } else {
            action = Intent.ACTION_EDIT;
            noteFilter = DBOpenHelper.NOTE_ID + "=" + uri.getLastPathSegment();

            Cursor cursor = getContentResolver().query(uri,
                    DBOpenHelper.ALL_COLUMNS, noteFilter, null, null);
            cursor.moveToFirst();
            oldText = cursor.getString(cursor.getColumnIndex(DBOpenHelper.NOTE_TEXT));
            editor.setText(oldText);
            mDisplayDate.setText(cursor.getString(cursor.getColumnIndex(DBOpenHelper.NOTE_CREATED)));
            editor.requestFocus();
        }
    }



    /**
     * Define method to get data from EditText into String variables
     **/
    public void GetInput() {
        TempPeak = editor.getText().toString();
        TempElevation = elevation.getText().toString();
        TempLat = lati.getText().toString();
        TempLong = longi.getText().toString();
    }


    /**
     * Define AsyncTask method class to Android PHP Insert Data to Server
     **/
    public void InsertData(final String peak, final String elevation, final String longitude, final String latitude) {

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                String PeakHolder = peak;
                String ElevationHolder = elevation;
                String LongHolder = longitude;
                String LatHolder = latitude;

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("peak", PeakHolder));
                nameValuePairs.add(new BasicNameValuePair("elevation", ElevationHolder));
                nameValuePairs.add(new BasicNameValuePair("longitude", LongHolder));
                nameValuePairs.add(new BasicNameValuePair("latitude", LatHolder));

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(ServerURL);
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse httpResponse = httpClient.execute(httpPost);
                    HttpEntity httpEntity = httpResponse.getEntity();

                } catch (ClientProtocolException e) {

                } catch (IOException e) {

                }

                return "Data Inserted Successfully!";
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                Toast.makeText(EditorActivity.this, "Data submit Successfully", Toast.LENGTH_LONG).show();
            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(peak, elevation, longitude, latitude);
    }


    private void displayLocation() {
        Latitude.setText("Latitude: " + currentLatitude);
        Longitude.setText("Longitude: " + currentLongitude);
    }

    private void init() {
        Button btnMap = (Button) findViewById(R.id.btnMap);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditorActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });
    }

    // Google Location Service
    public boolean isServicesOK() {
        Log.d(TAG, "isServicesOK: Checking Google Services Version");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(EditorActivity.this);

        if (available == ConnectionResult.SUCCESS) {
            //everything is fine and user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //an error occurred but we can resolve it
            Log.d(TAG, "isServicesOK: an error occurred but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(EditorActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            displayLocation();
        }
        return false;
    }


    //getData Button Function
    public void pullData() {
        Intent intent = new Intent(this, MobileVizActivity.class);
        startActivity(intent);
    }

    public void takePicture() {
        dispatchTakePictureIntent();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, 1);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        ImageName.setText("Image: " + mCurrentPhotoPath);
        return image;
    }

    private void syncLocal() {
        try {
            bm = BitmapFactory.decodeFile(mCurrentPhotoPath);
            executeMultipartPost();
        } catch (Exception e) {
            Log.e(e.getClass().getName(), e.getMessage());
        }
        try {
            URL url = new URL("http://192.168.1.2:5000/image_location_server/addDataPoint");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            File file = new File(mCurrentPhotoPath);

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("latitude", currentLatitude);
            jsonParam.put("longitude", currentLongitude);
            jsonParam.put("file_url", file.getName());

            Log.i("JSON", jsonParam.toString());
            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
            os.writeBytes(jsonParam.toString());

            os.flush();
            os.close();

            Log.i("STATUS", String.valueOf(conn.getResponseCode()));
            Log.i("MSG", conn.getResponseMessage());

            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void executeMultipartPost() throws Exception {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 75, bos);
            byte[] data = bos.toByteArray();
            HttpClient httpClient = new DefaultHttpClient();
            File file = new File(mCurrentPhotoPath);
            HttpPost postRequest = new HttpPost(
                    "http://192.168.1.2:5000/image_location_server/add_image");
            ByteArrayBody bab = new ByteArrayBody(data, file.getName());
            // File file= new File("/mnt/sdcard/forest.png");
            // FileBody bin = new FileBody(file);
            MultipartEntity reqEntity = new MultipartEntity(
                    HttpMultipartMode.BROWSER_COMPATIBLE);
            reqEntity.addPart("uploaded", bab);
            reqEntity.addPart("photoCaption", new StringBody("sfsdfsdf"));
            postRequest.setEntity(reqEntity);
            HttpResponse response = httpClient.execute(postRequest);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent(), "UTF-8"));
            String sResponse;
            StringBuilder s = new StringBuilder();

            while ((sResponse = reader.readLine()) != null) {
                s = s.append(sResponse);
            }
            System.out.println("Response: " + s);
        } catch (Exception e) {
            // handle exception here
            Log.e(e.getClass().getName(), e.getMessage());
        }
    }

    public void setLocation() {
        try {
            URL url = new URL("http://192.168.1.2:5000/fieldview/setLocation");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("latitude", currentLatitude);
            jsonParam.put("longitude", currentLongitude);

            Log.i("JSON", jsonParam.toString());
            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
            os.writeBytes(jsonParam.toString());

            os.flush();
            os.close();

            Log.i("STATUS", String.valueOf(conn.getResponseCode()));
            Log.i("MSG", conn.getResponseMessage());

            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (action.equals(Intent.ACTION_EDIT)) {
            getMenuInflater().inflate(R.menu.menu_main, menu);
        }
        return true;
    }

    // Open AR & VR Apps
    public void openViz_ar2d(View view) {
        Intent open2DARVizApp = getPackageManager().getLaunchIntentForPackage("com.visualab.Forteeners_2D_AR");
        open2DARVizApp.putExtra("localData", NotesProvider.getAllEntries());
        open2DARVizApp.putExtra("address", "http://192.168.0.195:8000/entry/");
        startActivity(open2DARVizApp);
    }

    public void openViz_ar3d(View view) {
        Intent open3DARVizApp = getPackageManager().getLaunchIntentForPackage("com.visualab.Forteeners_3D_AR");
        open3DARVizApp.putExtra("localData", NotesProvider.getAllEntries());
        open3DARVizApp.putExtra("address", "http://192.168.0.195:8000/entry/");
        startActivity(open3DARVizApp);
    }

    public void openViz_vr2d(View view) {
        Intent open2DVRVizApp = getPackageManager().getLaunchIntentForPackage("com.visualab.Forteeners_2D_VR");
        open2DVRVizApp.putExtra("localData", NotesProvider.getAllEntries());
        open2DVRVizApp.putExtra("address", "http://192.168.0.195:8000/entry/");
        startActivity(open2DVRVizApp);
    }

    public void openViz_3d(View view) {
        Intent open3DVizApp = getPackageManager().getLaunchIntentForPackage("com.visualab.Forteeners_3D_VR");
        open3DVizApp.putExtra("localData", NotesProvider.getAllEntries());
        open3DVizApp.putExtra("address", "http://192.168.0.195:8000/entry/");
        startActivity(open3DVizApp);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finishEditing();
                break;
            case R.id.action_delete:
                deleteNote();
                break;
        }
        return true;
    }

    private void deleteNote() {
        getContentResolver().delete(NotesProvider.CONTENT_URI,
                noteFilter, null);
        Toast.makeText(this, R.string.note_deleted,
                Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();

    }

    private void finishEditing() {
        String newText = editor.getText().toString().trim();
        String current_date = mDisplayDate.getText().toString().trim();

        switch (action) {
            case Intent.ACTION_INSERT:
                if (newText.length() == 0) {
                    setResult(RESULT_CANCELED);
                } else {
                    insertNote(newText, current_date, String.valueOf(currentLongitude), String.valueOf(currentLatitude));
                }
                break;
            case Intent.ACTION_EDIT:
                if (newText.length() == 0) {
                    deleteNote();
                } else if (oldText.equals(newText)) {
                    setResult(RESULT_CANCELED);
                } else {
                    updateNote(newText, current_date, String.valueOf(currentLongitude), String.valueOf(currentLatitude));
                }
        }
        finish();
    }

    // Get Current Data & Time
    private static String get_currentTime() {
        String currenttime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        return currenttime;
    }

    private void updateNote(String noteText, String current_time, String longitude, String latitude) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.NOTE_TEXT, noteText);
        values.put(DBOpenHelper.NOTE_CREATED, current_time);
        values.put(DBOpenHelper.NOTE_LOC_LONGITUDE, currentLongitude);
        values.put(DBOpenHelper.NOTE_LOC_LATITUDE, currentLatitude);

        getContentResolver().update(NotesProvider.CONTENT_URI, values, noteFilter, null);
        Toast.makeText(this, R.string.note_updated, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
    }

    private void insertNote(String noteText, String current_time, String longitude, String latitude) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.NOTE_TEXT, noteText);
        values.put(DBOpenHelper.NOTE_CREATED, current_time);
        values.put(DBOpenHelper.NOTE_LOC_LONGITUDE, currentLongitude);
        values.put(DBOpenHelper.NOTE_LOC_LATITUDE, currentLatitude);

        getContentResolver().insert(NotesProvider.CONTENT_URI, values);
        setResult(RESULT_OK);
    }



    //Finish Editing on Back Button pre
    @Override
    public void onBackPressed() {
        finishEditing();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Now lets connect to the API
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(this.getClass().getSimpleName(), "onPause()");

        //Disconnect from API onPause()
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }

    }

    /**
     * If connected get lat and long
     *
     */
    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (LocationListener) this);

        } else {
            //If everything went fine lets get latitude and longitude
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();

            displayLocation();
        }
    }
    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();
        displayLocation();
    }



    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
            /*
             * Google Play services can resolve some errors it detects.
             * If the error has a resolution, try sending an Intent to
             * start a Google Play services activity that can resolve
             * error.
             */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                    /*
                     * Thrown if Google Play services canceled the original
                     * PendingIntent
                     */
            } catch (IntentSender.SendIntentException e) {
                // Log the error f
                e.printStackTrace();
            }
        } else {
                /*
                 * If no resolution is available, display a dialog to the
                 * user with the error.
                 */
            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

}



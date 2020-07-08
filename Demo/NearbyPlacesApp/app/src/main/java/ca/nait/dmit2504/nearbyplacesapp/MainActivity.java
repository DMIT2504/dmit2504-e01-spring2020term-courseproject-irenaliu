package ca.nait.dmit2504.nearbyplacesapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    //logger
    private static final String TAG = MainActivity.class.getSimpleName();

    //================================================================================
    // Variables
    //================================================================================
    private Spinner mTypeSpinner;
    private Button mFindBtn;
    private GoogleMap mGoogleMap;

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private boolean mLocationPermissionsGranted = false;
    private GoogleApiClient mGoogleApiClient;

    private double mCurrentLat = 0.0, mCurrentLng = 0.0;
    public static final int REQUEST_LOCATION_CODE = 99;
    private static final float DEFAULT_ZOOM = 12;
    public static final int PROXIMITY_RADIUS = 10000; //in meters

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get views
        mTypeSpinner = findViewById(R.id.act_main_type_spinner);
        mFindBtn = findViewById(R.id.act_main_find_btn);

        //initialize arrays of place type & placename
        String[] placeTypeList = {"atm", "bank", "hospital", "movie_theater", "restaurant"};
        String[] placeNameList = {"ATM", "Bank", "Hospital", "Movie Theater", "Restaurant"};

        //set adapter for spinner
        mTypeSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, placeNameList));

        //check permissions
        getLocationPermissions();

        SupportMapFragment mSupportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.act_main_map_fragment);
        mSupportMapFragment.getMapAsync(MainActivity.this);

        mFindBtn.setOnClickListener(v -> {
            //get lat + lng from prefs
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            mCurrentLat = Double.parseDouble(prefs.getString("lat_pref", "0.00"));
            mCurrentLng = Double.parseDouble(prefs.getString("lng_pref", "0.00"));

            //build request url
            String url = buildRequestURL(placeTypeList);

            //execute place task to build request, download json data
            new PlaceTask().execute(url);
        });
    }

    //================================================================================
    // Custom Methods
    //================================================================================

    public void getLocationPermissions() {
        //get location permissions
        //the permissions we need
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                //permissions granted
                mLocationPermissionsGranted = true;
            } else {
                //need to ask for permissions
                ActivityCompat.requestPermissions(this, permissions, REQUEST_LOCATION_CODE);

                // REQUEST_LOCATION_CODE is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_LOCATION_CODE);
        }
    }

    //handle permission request response
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_CODE:
                //is permission granted?
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission granted
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mLocationPermissionsGranted = true;
                        // do location-related task
                        if(mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mGoogleMap.setMyLocationEnabled(true);
                    }
                } else {
                    //permission denied
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        if (mLocationPermissionsGranted) {
            //initialize fused location provider client
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            getCurrentLocation();
        }
    }

    public void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //store latitude + longitude in prefs
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();

        //initialize task location
        Task locationTask = mFusedLocationProviderClient.getLastLocation();
        locationTask.addOnCompleteListener(task -> {
            if(locationTask.isSuccessful()) {
                Location location = (Location) locationTask.getResult();

                //storing coordinates here
                editor.putString("lat_pref", String.valueOf(location.getLatitude()));
                editor.putString("lng_pref", String.valueOf(location.getLongitude()));
                editor.commit();

                //sync map
                //move camera to current location
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), DEFAULT_ZOOM));
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(new LatLng(location.getLatitude(), location.getLongitude()))
                        .title("My Location")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                mGoogleMap.addMarker(markerOptions);
            }
        });

    }

    private String buildRequestURL(String[] placeTypeList) {
        //get spinner position
        int i = mTypeSpinner.getSelectedItemPosition();

        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");

        //add required parameters & optional parameters
        googlePlaceUrl.append("location=" + mCurrentLat + "," + mCurrentLng);
        googlePlaceUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlaceUrl.append("&type=" + placeTypeList[i]);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key=" + getResources().getString(R.string.places_api_key));

        Log.d(TAG, googlePlaceUrl.toString());

        return googlePlaceUrl.toString();
    }

    private String downloadUrl(String myUrl) throws IOException {
        String data = "";
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;

        try {
            //initialize url
            URL url = new URL(myUrl);
            //initialize connection
            urlConnection = (HttpURLConnection) url.openConnection();
            //connect connection
            urlConnection.connect();

            //READ DATA
            //initialize input stream
            inputStream = urlConnection.getInputStream();
            //initialize buffer reader
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            //initialize string builder
            StringBuilder builder = new StringBuilder();
            //initialize string variable
            String  line = "";
            while((line = reader.readLine()) != null) {
                builder.append(line);
            }
            //get data
            data = builder.toString();
            //close reader
            reader.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            //always gets executed
            inputStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    //================================================================================
    // Inner Classes
    //================================================================================
    private class PlaceTask extends AsyncTask<String,Integer,String> {
        //AsyncTask<Params, Progress, Result>

        //initialize data
        String data = null;
        @Override
        protected String doInBackground(String... strings) {
            try {
                data = downloadUrl(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            //execute parser task
            new ParserTask().execute(s);
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>>{

        @Override
        protected List<HashMap<String, String>> doInBackground(String... strings) {
            //create custom json parser
            CustomJsonParser jsonParser = new CustomJsonParser();
            //initialize hash map list
            List<HashMap<String,String>> mapList = null;
            //initialize json object
            JSONObject object = null;
            try {
                object = new JSONObject(strings[0]);
                //parse object
                mapList = jsonParser.parseResult(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return mapList;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> placesList) {
           //clear map
            mGoogleMap.clear();
            for (int i = 0; i < placesList.size(); i++) {
                MarkerOptions markerOptions = new MarkerOptions();
                HashMap<String, String> googlePlace = placesList.get(i);

                String placeName = googlePlace.get("name");
                double lat = Double.parseDouble(googlePlace.get("lat"));
                double lng = Double.parseDouble(googlePlace.get("lng"));
                LatLng latLng = new LatLng(lat, lng);

                //set position and title
                markerOptions.position(latLng);
                markerOptions.title(placeName);
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));

                mGoogleMap.addMarker(markerOptions);
            }
        }
    }
}
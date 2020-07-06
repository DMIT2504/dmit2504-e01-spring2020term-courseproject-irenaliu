package ca.nait.dmit2504.outtolunch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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

public class MapActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION_CODE = 99;
    public static final int PROXIMITY_RADIUS = 5000;

    //initialize variables
    Spinner spType;
    Button btFind;
    SupportMapFragment mSupportMapFragment;
    GoogleMap mMap;
    FusedLocationProviderClient mFusedLocationProviderClient;
    double mCurrentLat = 0, mCurrentLong = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //assign variables
        spType = findViewById(R.id.sp_type);
        btFind = findViewById(R.id.bt_find);
        mSupportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);

        //initialize array of place type
        String[] placeTypeList = {"atm", "bank", "hospital", "movie_theater", "restaurant"};
        String[] placeNameList = {"ATM", "Bank", "Hospital", "Movie Theater", "Restaurant"};

        //set adapter
        spType.setAdapter((new ArrayAdapter<>(MapActivity.this,
                android.R.layout.simple_spinner_dropdown_item, placeNameList)));

        //initialize fused location provider client
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //check permission
        if (ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //permission granted
            getCurrentLocation();
        } else {
            //when permission denied
            ActivityCompat.requestPermissions(MapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
        }

        btFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get selected position of spinner
                int i = spType.getSelectedItemPosition();
                //initialize url
                String url = getUrl(mCurrentLat,mCurrentLong, placeTypeList[i]);

                //execute place task method to download json data
                new PlaceTask().execute(url);
            }
        });
    }

    private String getUrl(double latitude, double longitude, String nearbyPlace) {
        //https://maps.googleapis.com/maps/api/place/nearbysearch/output?parameters
        //can be json or xml
        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");

        //add parameters
        googlePlaceUrl.append("location=" + latitude + "," + longitude);
        googlePlaceUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlaceUrl.append("&type=" + nearbyPlace);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key=" + getResources().getString(R.string.google_maps_key));

        return googlePlaceUrl.toString();
    }

    private void getCurrentLocation() {
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
        Task<Location> task = mFusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(location -> {
            //on success
            if (location != null) {
                //when location not null
                //get GPS coordinates
                mCurrentLat = location.getLatitude();
                mCurrentLong = location.getLongitude();
                //sync Map
                mSupportMapFragment.getMapAsync(googleMap -> {
                    //when map is ready
                    mMap = googleMap;
                    //zoom current location on map
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(mCurrentLat,mCurrentLong), 10
                    ));
                });
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_CODE:
                //is permission granted?
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission granted
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        getCurrentLocation();
                    }
                } else {
                    //permission denied
                    Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    private class PlaceTask extends AsyncTask<String,Integer,String> {
        @Override
        protected String doInBackground(String... strings) {
            String data = null;
            try {
                //initialize data
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

    private class ParserTask extends AsyncTask<String,Integer, List<HashMap<String,String>>> {

        @Override
        protected List<HashMap<String, String>> doInBackground(String... strings) {
            //create json parser class
            JsonParser jsonParser = new JsonParser();
            //initialize hash map list
            List<HashMap<String,String>> mapList = null;
            //initialize json object
            JSONObject object = null;
            try {
                //initialize json object
                object = new JSONObject(strings[0]);
                //parse json object
                //mapList = jsonParser.parseResult(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return mapList;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> hashMaps) {
            //clear map
            mMap.clear();
            for(int i = 0; i < hashMaps.size(); i++) {
                //initialize hash map
                HashMap<String,String> hashMapList = hashMaps.get(i);
                //get lattitude & longitude
                double lat = Double.parseDouble(hashMapList.get("lat"));
                double lng = Double.parseDouble(hashMapList.get("lng"));
                //get name
                String name = hashMapList.get("name");
                //concat lattitude and longitude
                LatLng latLng = new LatLng(lat,lng);
                //initialize marker options
                MarkerOptions options = new MarkerOptions();
                //set position
                options.position(latLng);
                //set title
                options.title(name);
                //add marker on map
                mMap.addMarker(options);
            }
        }
    }
}
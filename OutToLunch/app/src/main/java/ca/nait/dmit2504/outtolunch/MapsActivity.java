package ca.nait.dmit2504.outtolunch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.tasks.Task;


public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    //logger
    private static final String TAG = MapsActivity.class.getSimpleName();

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient; //maybe not required
    private boolean mLocationPermissionsGranted = false;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private Location mLastLocation;
    private Marker mCurrentLocationMarker;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static final float DEFAULT_ZOOM = 12;
    public static final int REQUEST_LOCATION_CODE = 99;
    public static final int PROXIMITY_RADIUS = 500;
    private double mLat = 0.0, mLng = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //check permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getLocationPermission();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (mLocationPermissionsGranted) {
            getDeviceLocation();

            if (getIntent() != null) {
                String searchString = getIntent().getStringExtra("search");
                NearbyPlacesTask getNearbyPlacesData = new NearbyPlacesTask();

                if (!searchString.equals("")) {
                    //search
                    mMap.clear();
                    String url = getUrl(mLat, mLng, searchString); //need to get latitude and longitude
                    Object dataTransfer[] = new Object[2];
                    dataTransfer[0] = mMap;
                    dataTransfer[1] = url;
                    getNearbyPlacesData.execute(dataTransfer);
                    Toast.makeText(getApplicationContext(), "Showing nearby restaurants", Toast.LENGTH_SHORT).show();
                }
            }

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
    }

    public void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
                initMap();
            } else {
                //ask for permissions
                ActivityCompat.requestPermissions(this, permissions, REQUEST_LOCATION_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_LOCATION_CODE);
        }
    }

    private void initMap() {
        Log.d(TAG, "initMap: initializing map");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void moveCamera(LatLng latlng, float zoom, String title) {
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latlng.latitude + ", lng: " + latlng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoom));

        if(!title.equals("My Location")) {
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latlng)
                    .title(title)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            mMap.addMarker(markerOptions);
        }
    }

    private String getUrl(double latitude, double longitude, String cuisine) {
        //https://maps.googleapis.com/maps/api/place/nearbysearch/output?parameters
        //OR
        //https://maps.googleapis.com/maps/api/place/textsearch/output?parameters
        //can be json or xml
        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/textsearch/json?");
        String restaurantType = "";
        if (cuisine.equals("American")) {
            restaurantType = "pizza+burgers+fries+restaurant";
        } else {
            restaurantType = cuisine + "+restaurant";
        }

        //add parameters
        //sample lat & lng = 53.4604314, -113.56062109999999
        googlePlaceUrl.append("query=" + restaurantType);
        googlePlaceUrl.append("&location=" + 53.4604314 + "," + -113.56062109999999);
        googlePlaceUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlaceUrl.append("&type=restaurant+food");
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key=" + getResources().getString(R.string.google_places_key));

        Log.d(TAG, googlePlaceUrl.toString());

        return googlePlaceUrl.toString();
    }

    //handle permission request response
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_CODE:
                //is permission granted?
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission granted
                    // do location-related task
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            if (mGoogleApiClient == null) {
                                buildGoogleApiClient();
                            }
                            mMap.setMyLocationEnabled(true);
                        }
                    }
                } else {
                    //permission denied
                    Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    private void getDeviceLocation() {
        Log.d(TAG, "GetDeviceLocation: getting the devices current location");
        //initialize fused location provider client
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (mLocationPermissionsGranted) {
                Task locationTask = mFusedLocationProviderClient.getLastLocation();
                locationTask.addOnCompleteListener(task -> {
                    if (locationTask.isSuccessful()) {
                        Log.d(TAG, "onComplete: found location");
                        Location currentLocation = (Location) locationTask.getResult();
                        moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                DEFAULT_ZOOM,
                                "Current Location");
                    } else {
                        Log.d(TAG, "onComplete: current location is null");
                        Toast.makeText(MapsActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
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
    public void onLocationChanged(Location location) {
        mLat = location.getLatitude();
        mLng = location.getLongitude();
        mLastLocation = location;

        //clear current location marker
        if (mCurrentLocationMarker != null) {
            mCurrentLocationMarker.remove();
        }

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        //move map
        moveCamera(latLng, DEFAULT_ZOOM, "New location");

        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    //when device is connected
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1100); //milliseconds
        mLocationRequest.setFastestInterval(1100);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (mFusedLocationProviderClient != null) {
            mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
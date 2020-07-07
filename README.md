# Place Search - Using Google Places API
> By Irena Liu

> Last updated: July 7th, 2020

This document outlines a general guideline for how to set up a project to use Google's Place Search using the Google Places API. The Place Search will return a list of places based on a user's location or search string.

---
## Overview

1. Setting Up Project/ Getting Started
2. Create a Map Fragment in the Layout
3. Check Mapping Permissions
4. Get Current Location
5. Construct the Request URL
6. Execute Request Url
Official Documentation / Helpful Videos

---
## 1 - Setting Up Project/ Getting Started

### Step 1) Create a new Android Studio project
Create an Empty Activity. If you choose to create a Maps Activity, the steps will be a bit different.

### Step 2) Import Google-play-services SDK
Click on Tools > SDK Manager, then click on the SDK Tools tab. Make sure "Google Play services" is installed.

![Android SDK Manager](/Pictures-For-Notes/sdk-manager.jpg)

Next, we need to add Google Play Services to the new project. Go to your build.gradle file and add the following Google Services dependencies. Click Sync in the top right after adding.
```java
    //Google Services Dependencies
    implementation 'com.google.android.gms:play-services-maps:17.0.0'
    implementation 'com.google.android.gms:play-services-location:17.0.0'
    implementation 'com.google.android.gms:play-services-nearby:17.0.0'
```

*Alternatively:* 

You can add Google Play Services in the following way too. Click on File > Project Structure, then the plus icon followed by "Library Dependency" to search for the dependencies required. Search for play-services-maps, play-services-location, and play-services-nearby. Add the most up-to-date versions which should be 17.0.0. Make sure you add all the same versions.

![Project Structure Dependencies Screen](/Pictures-For-Notes/project-structure.jpg)


### Step 3) Get a Google Maps API key and a Google Places API key
Before you begin: Before you start using the Places API, you need a project with a billing account and the Places API enabled. To learn more, read "Get Started with Google Maps Platform" in the official documentation I've linked below.

For now, click [here](https://cloud.google.com/maps-platform/#get-started) to go to the Google Console. You will need to login to your Google Account.

After you agree to the terms of service, it should take you to the overview page of the Google Cloud Platform. You can explore other Google APIs here.

![Google Cloud Platform](/Pictures-For-Notes/google-cloud-platform.jpg)

Next, create a new project by clicking on "Select a project" in the upper left hand part of the screen and selecting **"New Project"**.

Google Places API will need to be enabled. Click on Marketplace in the left hand pane then search for Places API. Click on **Enable**.

Next, we are going to create the two API keys. Click on "Credentials" in the left hand pane, then click on the link on the page, **"Credentials in APIs & Services"**. Click on Create Credential to create a new API key.

![Create Google Credential](/Pictures-For-Notes/create-credential.jpg)

Copy the API key, and for best practices, you should restrict your API key to the API it will be used for to make it secure. This one  will be for Google Maps. Save this key as a string resource in the **strings.xml** file.

Create another key for Google Places. Restrict the key and save this key as a string resource too.

![Save API key as String Resource](Pictures-For-Notes/saved-key.jpg)

*Note: If you have a MapsActivity, paste the Google Maps key into the **google_maps_api.xml** resource file created in your android project.*

### Step 4) Add uses-permissions & the Google Maps API key to AndroidManifest.xml
Insert the following code snippet in the AndroidManifest.xml as a child of the `<application>` element.

```java
<meta-data
            android:name="com.google.android.geo.API_KEY"
            android:value="@string/google_maps_key" />
```
Add the following 5 uses-permissions right before the start tag of `<application>`:

```java
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="com.google.android.providers.gsf.permission.READ_GSERVICES" />
```
## 2 - Create a Map Fragment in the Layout
Create a map fragment in the activity layout file. This fragment is the simplest way to place a map in an application.

```java
    <fragment
        android:name="com.google.android.gms.maps.MapFragment"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
```

Get the map fragment view in the onCreate method of the activity:
```java
    //variable
    private SupportMapFragment mSupportMapFragment;

    //get view in the onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        
        mSupportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.act_main_map_fragment);
    }

```

## 3 - Check Mapping Permissions
Check that the user has allowed the app access to use location services.
The location permission you will need to check for is the ACCESS_FINE_LOCATION. I would also recommend checking for the ACCESS_COARSE_LOCATION permission.

Example of how to check for the permissions:
```java
    //the permissions we need
    String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    if (ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //permissions granted
        } else {
            //need to ask for permissions
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, permissions, REQUEST_LOCATION_CODE);

                // REQUEST_LOCATION_CODE is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    } else {
        ActivityCompat.requestPermissions(this, permissions, REQUEST_LOCATION_CODE);
    }
```
You will need to handle the result of requesting permissions with an override method.

Example of request permissions response handler:
```java
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_CODE:
                //is permission granted?
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission granted
                    // do location-related task
                    getCurrentLocation();
                } else {
                    //permission denied
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }
```

## 4 - Get Current Location

### Step 1) Ensure Google Play services is installed on the device (Recommended)
Create an instance of GoogleApiClient.

The GoogleApiClient object provides the main entry point for Google Play services integration. The class has been deprecated but it will still work. It is suggested to use GoogleApi based APIs instead such as the new **GoogleSignInClient** class which includes authentication and does not require waiting for multiple callbacks. For more info, click [here](https://developers.google.com/android/reference/com/google/android/gms/common/api/GoogleApi).

GoogleApiClient instances are not thread-safe. To access Google APIs from multiple threads simultaneously, create a GoogleApiClient on each thread. GoogleApiClient service connections are cached internally, so creating multiple instances is fast. 

### Step 2) Get the last known location
FusedLocationProviderClient is the replacement for FusedLocationProviderApi which is used to get the last known GPS location. It must be initialized in the activity onCreate method.

```java
    //initializing the fused location provider client
    mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
```

Example of how to get the last location:
```java
    Task locationTask = mFusedLocationProviderClient.getLastLocation();
    locationTask.addOnCompleteListener(task -> {
        if (locationTask.isSuccessful()) {
            //found location!
            Location currentLocation = (Location) locationTask.getResult();
        } else {
            //location not found
            Toast.makeText(MapsActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
        }
    });
```

### Step 3) Sync map when the map is loaded
Get the map asynchronously and sync the map to the current location of the user when the map is ready.

```java
    //sync map
    mSupportMapFragment.getMapAsync(new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            //when map is ready
            mGoogleMap = googleMap;

            //move camera to current location
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mCurrentLat, mCurrentLat), DEFAULT_ZOOM));
        }
    });
```

## 5 - Construct the Request Url
There are 3 different types of request urls depending on what kind of data you are hoping to return.

    **3 Options for Request Urls:**
    Data can be outputted as JSON or xml

    https://maps.googleapis.com/maps/api/place/findplacefromtext/output?parameters
    
    https://maps.googleapis.com/maps/api/place/nearbysearch/output?parameters

    https://maps.googleapis.com/maps/api/place/textsearch/output?parameters


#### Required parameters & Optional parameters

#### Find Place vs. Nearby Search vs. Text Search
- Something here

#### Example Request Urls
- Something here

## 6 - Execute Request Url

---
## Official Documentation & Other Helpful Videos

### Documentation

[Intro to Google Places](https://developers.google.com/places/web-service/intro)

[Getting Started with Google Maps Platform](https://developers.google.com/maps/gmp-get-started)

[MapFragments](https://developers.google.com/android/reference/com/google/android/gms/maps/MapFragment)

[ContextCompat vs. ActivityCompat](https://stackoverflow.com/questions/42832847/what-is-the-difference-between-contextcompat-checkselfpermission-and-activityc)

[Moving Past GoogleApiClient](https://android-developers.googleblog.com/2017/11/moving-past-googleapiclient_21.html)

[FusedLocationProviderClient](https://developers.google.com/android/reference/com/google/android/gms/location/FusedLocationProviderClient)

[Place Search Request Urls](https://developers.google.com/places/web-service/search)

### YouTube Video Tutorials

[How to Find Nearby Places on Map in Android Studio](https://youtu.be/pjFcJ6EB8Dg)


# Place Search - Using Google Places API
> By Irena Liu

> Last updated: July 7th, 2020

This document outlines a general guideline for how to set up a project to use Google's Place Search using the Google Places API. The Place Search will return a list of places based on a user's location or search string.

----------------------------------------------
## Setting Up/ Getting Started

### Step 1) Create a new Android Studio project
Create an empty Activity as well as a MapsActivity.

### Step 2) Import Google-play-services SDK
Click on Tools > SDK Manager, then click on the SDK Tools tab. Make sure "Google Play services" is installed.

![Android SDK Manager](/Pictures-For-Notes/sdk-manager.jpg)

Next, we need to add Google Play Services to the new project. Go to your build.gradle file and add the following Google Services dependencies 
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

Copy the API key, and for best practices, you should restrict your API key to the API it will be used for to make it secure. This one  will be for Google Maps.

Paste the copied key in the **google_maps_api.xml** resource file in your android project.

Create another key for Google Places. Restrict the key and save this key as a string resource too but in the regular **strings.xml** file.

![Save API key as String Resource](Pictures-For-Notes/saved-key.jpg)

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

## Check for Mapping Permissions

## Set up Device Location / Get Current Location

## Construct the Request Url
    https://maps.googleapis.com/maps/api/place/findplacefromtext/output?parameters
    
    https://maps.googleapis.com/maps/api/place/nearbysearch/output?parameters
    
    OR

    https://maps.googleapis.com/maps/api/place/textsearch/output?parameters

    Can be outputted as json or xml

Required parameters & Optional parameters

#### Find Place vs. Nearby Search vs. Text Search
- Something here

#### Example Request Urls
- Something here

## Official Documentation & Other Helpful Videos
Intro to Google Places
https://developers.google.com/places/web-service/intro
Getting Started with Google Maps Platform
https://developers.google.com/maps/gmp-get-started
Place Search Request Urls
https://developers.google.com/places/web-service/search
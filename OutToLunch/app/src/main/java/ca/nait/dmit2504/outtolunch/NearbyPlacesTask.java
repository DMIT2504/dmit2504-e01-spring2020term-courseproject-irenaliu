package ca.nait.dmit2504.outtolunch;

import android.os.AsyncTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class NearbyPlacesTask extends AsyncTask<Object, String, String> {

    String mGooglePlacesData = null;
    GoogleMap mMap;
    String mUrl;

    @Override
    protected String doInBackground(Object... objects) {
        mMap = (GoogleMap) objects[0];
        mUrl = (String) objects[1];

        DownloadUrl downloadUrl = new DownloadUrl();
        try {
            //initialize data
            mGooglePlacesData = downloadUrl.readUrl(mUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mGooglePlacesData;
    }

    @Override
    protected void onPostExecute(String s) {
        List<HashMap<String, String>> nearbyPlaceList = null;
        JsonParser parser = new JsonParser();
        //execute parser task
        nearbyPlaceList = parser.parseResult(s);
        showNearbyPlaces(nearbyPlaceList);
    }


    private void showNearbyPlaces(List<HashMap<String, String>> nearbyPlaceList) {
        for (int i = 0; i < nearbyPlaceList.size(); i++) {
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String, String> googlePlace = nearbyPlaceList.get(i);

            String placeName = googlePlace.get("name");
            String vicinity = googlePlace.get("vicinity");
            double lat = Double.parseDouble(googlePlace.get("lat"));
            double lng = Double.parseDouble(googlePlace.get("lng"));
            LatLng latLng = new LatLng(lat, lng);

            //set position and title
            markerOptions.position(latLng);
            markerOptions.title(placeName + " : " + vicinity);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

            mMap.addMarker(markerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
        }
    }
}

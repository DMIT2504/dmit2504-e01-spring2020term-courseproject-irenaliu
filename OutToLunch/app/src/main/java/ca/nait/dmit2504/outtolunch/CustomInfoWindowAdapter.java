package ca.nait.dmit2504.outtolunch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View mWindow;
    private Context mContext;

    public CustomInfoWindowAdapter(Context context) {
        mContext = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.custom_info_window, null);
    }

    private void renderWindowText(Marker marker, View view) {
        String title = marker.getTitle();
        String snippet = marker.getSnippet();
        TextView titleTxt = view.findViewById(R.id.custom_info_window_title);
        TextView snippetTxt = view.findViewById(R.id.custom_info_window_snippet);

        if (!title.equals("")) {
            titleTxt.setText(title);
        }

        if(title.equals("Current Location")) {
            snippetTxt.setText("");
        } else {
            if (!snippet.equals("")) {
                snippetTxt.setText(snippet);
            }
        }
    }

    @Override
    public View getInfoWindow(Marker marker) {
        renderWindowText(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        renderWindowText(marker, mWindow);
        return mWindow;
    }
}

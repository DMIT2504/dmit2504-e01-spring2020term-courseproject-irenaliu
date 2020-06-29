package ca.nait.dmit2504.outtolunch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText mSearchEdit;
    private Button mFindBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchEdit = findViewById(R.id.act_main_search_edit);
        mFindBtn = findViewById(R.id.act_main_search_btn);

        mFindBtn.setOnClickListener(v -> {
            String searchString = mSearchEdit.getText().toString();

            if(!searchString.equals("")) {
                Intent mapIntent = new Intent(this, MapsActivity.class);
                mapIntent.putExtra("search", searchString);
                startActivity(mapIntent);
            }
        });
    }
}
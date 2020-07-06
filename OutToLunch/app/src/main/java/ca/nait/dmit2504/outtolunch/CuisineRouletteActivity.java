package ca.nait.dmit2504.outtolunch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class CuisineRouletteActivity extends AppCompatActivity {

    private EditText mSearchEdit;
    private Button mFindBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuisine_roulette);

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
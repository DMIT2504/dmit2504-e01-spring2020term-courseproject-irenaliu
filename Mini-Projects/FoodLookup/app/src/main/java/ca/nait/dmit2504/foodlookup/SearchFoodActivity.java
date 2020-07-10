package ca.nait.dmit2504.foodlookup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;

public class SearchFoodActivity extends AppCompatActivity {

    private EditText mFoodEdit;
    private ImageButton mFindBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_food);

        mFoodEdit = findViewById(R.id.act_search_food_edit);
        mFindBtn = findViewById(R.id.act_search_find_btn);

        mFindBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String foodQuery = mFoodEdit.getText().toString();
                if(!foodQuery.equals("")) {
                    Intent foodIntent = new Intent(getApplicationContext(), FoodFragmentActivity.class);
                    foodIntent.putExtra("food", foodQuery);
                    startActivity(foodIntent);
                }
            }
        });
    }
}
package ca.nait.dmit2504.foodlookup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class FoodFragmentActivity extends AppCompatActivity {

    private String mFoodName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_fragment);

        mFoodName = getIntent().getStringExtra("food");

        //create an instance of the food
        FoodFragment foodFragment = new FoodFragment();
        Bundle bundle = new Bundle();
        bundle.putString("food", mFoodName);
        foodFragment.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .commit();
    }
}
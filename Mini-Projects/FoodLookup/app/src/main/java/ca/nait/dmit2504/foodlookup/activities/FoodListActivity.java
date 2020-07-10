package ca.nait.dmit2504.foodlookup.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ca.nait.dmit2504.foodlookup.BaseActivity;
import ca.nait.dmit2504.foodlookup.FoodListViewAdapter;
import ca.nait.dmit2504.foodlookup.R;
import ca.nait.dmit2504.foodlookup.foodapi.Hints;
import ca.nait.dmit2504.foodlookup.foodapi.Root;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodListActivity extends BaseActivity {

    private ListView mFoodListView;
    private ArrayList<Hints> mFoodResultList;

    private String mFoodName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        if (!getIntent().getStringExtra("food").equals("")) {
            mFoodName = getIntent().getStringExtra("food");
        }

        mFoodListView = findViewById(R.id.act_food_list_results_listview);
//        mFoodListView.setOnItemClickListener((adapterView, view, i, l) -> {
//            Intent viewFoodIntent = new Intent(getApplicationContext(), FoodViewPagerActivity.class);
//            viewFoodIntent.putExtra("foodID", mFoodResultList.get(i).getFood().getFoodId());
//            startActivity(viewFoodIntent);
//        });

        mFoodResultList = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getFoods();
    }

    private void getFoods() {
        Call<Root> getFoodsCall = mFoodService.getFoods(mFoodName, getResources().getString(R.string.edamam_appId), getResources().getString(R.string.edamam_api_key));
        Log.i("FoodListActivity", getFoodsCall.toString());
        getFoodsCall.enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                Root responseResults = response.body();
                if (response.isSuccessful()) {
                    mFoodResultList = new ArrayList<>();
                    //get the 'hints' from result of response body
                    List<Hints> results = responseResults.getHints();
                    mFoodResultList.addAll(results);

                    FoodListViewAdapter adapter = new FoodListViewAdapter(mFoodResultList);
                    mFoodListView.setAdapter(adapter);
                } else {
                    Toast.makeText(getApplicationContext(), "Unable to fetch foods", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Unable to fetch foods", Toast.LENGTH_LONG).show();
            }
        });
    }
}
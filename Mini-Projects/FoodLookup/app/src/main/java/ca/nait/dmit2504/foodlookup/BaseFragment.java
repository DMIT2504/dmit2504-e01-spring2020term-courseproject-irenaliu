package ca.nait.dmit2504.foodlookup;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ca.nait.dmit2504.foodlookup.foodapi.FoodApiService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BaseFragment extends Fragment {
    private static final String BASE_URL = "https://api.edamam.com/api/food-database/v2/";
    protected FoodApiService mFoodService;
    protected Retrofit mRetrofit;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mFoodService = mRetrofit.create(FoodApiService.class);
    }
}

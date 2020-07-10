package ca.nait.dmit2504.foodlookup.foodapi;

import ca.nait.dmit2504.foodlookup.foodapi.Food;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FoodApiService {
    @GET("parser")
    Call<Root> getFoods(@Query("food") String food, @Query("appId") String appId, @Query("key") String key);
}

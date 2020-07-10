package ca.nait.dmit2504.foodlookup;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import ca.nait.dmit2504.foodlookup.foodapi.Food;

public class FoodFragmentStateAdapter extends FragmentStateAdapter {

    public String mFoodName;

    public FoodFragmentStateAdapter(@NonNull FragmentActivity fragmentActivity, String foodName) {
        super(fragmentActivity);
        mFoodName = foodName;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        switch (position) {
            case 1:
                fragment = new DetailsFragment();
                break;
            case 2:
                fragment = new NutritionFragment();
                break;
            default:
                fragment = new FoodFragment();
                break;
        }

        Bundle args = new Bundle();
        args.putString("food", mFoodName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}

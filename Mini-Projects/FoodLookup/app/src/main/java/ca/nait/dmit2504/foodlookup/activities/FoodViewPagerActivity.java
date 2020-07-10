package ca.nait.dmit2504.foodlookup.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import ca.nait.dmit2504.foodlookup.BaseActivity;
import ca.nait.dmit2504.foodlookup.FoodFragmentStateAdapter;
import ca.nait.dmit2504.foodlookup.R;

public class FoodViewPagerActivity extends BaseActivity {

    private String mFoodName;
    private ViewPager2 mViewPager;
    private FoodFragmentStateAdapter mStateAdapter;
    private String[] mTabTitles = new String[]{"Pokemon", "Moves", "Abilities", "Stats"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_view_pager);

        mFoodName = getIntent().getStringExtra("food");

        mViewPager = findViewById(R.id.activity_food_view_pager_viewPager);
        mStateAdapter = new FoodFragmentStateAdapter(this, mFoodName);
        mViewPager.setAdapter(mStateAdapter);

        TabLayout tabLayout = findViewById(R.id.activity_food_view_pager_tabLayout);
        new TabLayoutMediator(tabLayout, mViewPager,
                (tab, position) -> {
                    tab.setText(mTabTitles[position]);
                }).attach();
    }
}
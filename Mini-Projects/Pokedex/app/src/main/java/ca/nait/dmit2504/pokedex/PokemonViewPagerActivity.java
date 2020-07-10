package ca.nait.dmit2504.pokedex;

import android.os.Bundle;

import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class PokemonViewPagerActivity extends BaseActivity {

    private String mPokemonName;
    private ViewPager2 mPokemonViewPager;
    private PokemonFragmentStateAdapter mPokemonFragmentStateAdapter;
    private String[] mTabTitles = new String[]{"Pokemon", "Moves", "Abilities", "Stats"};
    private TabLayout mTabViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_view_pager);

        //get pokemon name
        mPokemonName = getIntent().getStringExtra("NAME");
        mTabViews = findViewById(R.id.activity_pokemon_view_pager_tabLayout);

        //get views
        mPokemonViewPager = findViewById(R.id.activity_pokemon_view_pager_viewPager);

        //set views
        mPokemonFragmentStateAdapter = new PokemonFragmentStateAdapter(this, mPokemonName);
        mPokemonViewPager.setAdapter(mPokemonFragmentStateAdapter);

        new TabLayoutMediator(mTabViews, mPokemonViewPager,
                (tab, position) -> {
                    tab.setText(mTabTitles[position]);
                }).attach();
    }
}
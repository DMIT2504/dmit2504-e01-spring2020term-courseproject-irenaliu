package ca.nait.dmit2504.pokedex;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ca.pokeapi.PokedexReturn;
import ca.pokeapi.Result;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewPokedexActivity extends BaseActivity {

    private static final int mNumberOfPokemon = 151;

    private ListView mPokedexListview;
    private ArrayList<Result> mPokedex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pokedex);

        //find views
        mPokedexListview = findViewById(R.id.act_view_pokedex_listview);
        mPokedexListview.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(getApplicationContext(), PokemonViewPagerActivity.class);
            intent.putExtra("NAME", mPokedex.get(position).getName());
            startActivity(intent);
        });

        mPokedex = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getPokedex();
    }

    private void getPokedex() {
        Call<PokedexReturn> getCall = mPokeService.getAllPokemon(mNumberOfPokemon);
        getCall.enqueue(new Callback<PokedexReturn>() {
            @Override
            public void onResponse(Call<PokedexReturn> call, Response<PokedexReturn> response) {
                PokedexReturn responseBody = response.body();
                if (response.isSuccessful()) {
                    mPokedex = new ArrayList<>();
                    List<Result> results = responseBody.getResults();
                    mPokedex.addAll(results);

                    //set pokedex adapter
                    PokedexListViewAdapter pokedexListViewAdapter = new PokedexListViewAdapter(mPokedex);
                    mPokedexListview.setAdapter(pokedexListViewAdapter);
                } else {
                    Toast.makeText(getApplicationContext(), "Pokedex Fetch Unsuccessful", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<PokedexReturn> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Pokedex Fetch Failure", Toast.LENGTH_LONG).show();
            }
        });
    }
}
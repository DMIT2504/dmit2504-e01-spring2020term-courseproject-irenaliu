package ca.nait.dmit2504.pokedex.pokeapi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PokeApiService {
    @GET("pokemon")
    Call<PokedexReturn> getAllPokemon(@Query("limit") int limit);

    @GET("pokemon/{name}")
    Call<Pokemon> getPokemonByName(@Path("name") String name);

    @GET("pokemon/{id}")
    Call<Pokemon> getPokemonById(@Path("id") int id);
}

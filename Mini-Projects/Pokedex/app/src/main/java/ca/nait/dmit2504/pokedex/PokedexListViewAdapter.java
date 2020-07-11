package ca.nait.dmit2504.pokedex;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ca.pokeapi.Result;

public class PokedexListViewAdapter extends BaseAdapter {

    private final String URL_FORMAT = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/%d.png";
    private ArrayList<Result> mResults;

    public PokedexListViewAdapter(ArrayList<Result> results) {
        mResults = results;
    }

    @Override
    public int getCount() {
        return mResults.size();
    }

    @Override
    public String getItem(int position) {
        return mResults.get(position).getName();
    }

    @Override
    public long getItemId(int position) {
        return position + 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View rowView = inflater.inflate(R.layout.listview_pokemon_item, parent, false);

        //get and set pokemon item
        TextView nameTxt = rowView.findViewById(R.id.listview_pokemon_name_txt);
        TextView numTxt = rowView.findViewById(R.id.listview_pokemon_num_txt);
        ImageView pokemonImg = rowView.findViewById(R.id.listview_pokemon_img);

        nameTxt.setText(mResults.get(position).getName());
        numTxt.setText("#" + (position + 1));

        //load image
        Picasso.get()
                .load(String.format(URL_FORMAT, position + 1))
                .resize(500, 500)
                .centerCrop()
                .into(pokemonImg);

        return rowView;
    }
}

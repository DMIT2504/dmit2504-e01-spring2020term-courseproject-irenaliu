package ca.nait.dmit2504.pokedex.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ca.nait.dmit2504.pokedex.R;
import ca.pokeapi.Pokemon;
import ca.pokeapi.Type;

public class TypeAdapter extends BaseAdapter {

    private Pokemon mPokemon;
    private List<Type> mTypes;

    public TypeAdapter(Pokemon pokemon) {
        mPokemon = pokemon;
        mTypes = pokemon.getTypes();
    }

    @Override
    public int getCount() {
        return mTypes.size();
    }

    @Override
    public Object getItem(int position) {
        return mTypes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View typeView = inflater.inflate(R.layout.view_types, parent, false);

        TextView nameView = typeView.findViewById(R.id.view_type_name);
        nameView.setText(mTypes.get(position).getType().getName());

        TextView slotView = typeView.findViewById(R.id.view_types_slot);
        slotView.setText(mTypes.get(position).getSlot().toString());

        return typeView;
    }
}

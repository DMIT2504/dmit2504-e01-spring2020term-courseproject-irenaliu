package ca.nait.dmit2504.pokedex.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ca.nait.dmit2504.pokedex.R;
import ca.pokeapi.Ability;
import ca.pokeapi.Pokemon;

public class AbilitiesAdapter extends BaseAdapter {

    Pokemon mPokemon;
    List<Ability> mAbilities;

    public AbilitiesAdapter(Pokemon pokemon) {
        mPokemon = pokemon;
        mAbilities = mPokemon.getAbilities();
    }

    @Override
    public int getCount() {
        return mAbilities.size();
    }

    @Override
    public Ability getItem(int position) {
        return mAbilities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View abilityView = inflater.inflate(R.layout.view_abilities, parent, false);

        TextView nameTextView = abilityView.findViewById(R.id.view_abilities_name);
        nameTextView.setText(mAbilities.get(position).getAbility().getName());

        TextView slotTextView = abilityView.findViewById(R.id.view_abilities_slot);
        TextView hiddenTextView = abilityView.findViewById(R.id.view_abilities_hidden);

        if (mAbilities.get(position).getIsHidden()) {
            hiddenTextView.setText("hidden");
        } else {
            hiddenTextView.setText("");
        }
        slotTextView.setText(mAbilities.get(position).getSlot().toString());

        return abilityView;
    }
}

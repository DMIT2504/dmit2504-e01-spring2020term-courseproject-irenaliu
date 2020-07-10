package ca.nait.dmit2504.foodlookup;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ca.nait.dmit2504.foodlookup.foodapi.Hints;

public class FoodListViewAdapter extends BaseAdapter {

    private final String PICTURE_URL = "";
    private ArrayList<Hints> mResults;

    public FoodListViewAdapter(ArrayList<Hints> results) {
        mResults = results;
    }

    @Override
    public int getCount() {
        return mResults.size();
    }

    @Override
    public String getItem(int i) {
        return mResults.get(i).getFood().getFoodId(); //return foodID which is a string
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View rowView = inflater.inflate(R.layout.food_list_item, parent, false);

        TextView nameTxt = rowView.findViewById(R.id.food_list_item_name_txt);
        TextView categoryTxt = rowView.findViewById(R.id.food_list_item_category_txt);
        ImageView itemImg = rowView.findViewById(R.id.food_list_item_img);

        nameTxt.setText(mResults.get(i).getFood().getLabel());
        categoryTxt.setText(mResults.get(i).getFood().getCategory());
        //load img
        Picasso.get()
                .load(String.format(mResults.get(i).getFood().getImage()))
                .resize(500, 500)
                .centerCrop()
                .into(itemImg);

        return rowView;
    }
}

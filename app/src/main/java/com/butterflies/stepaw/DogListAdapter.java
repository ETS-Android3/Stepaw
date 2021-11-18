package com.butterflies.stepaw;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.butterflies.stepaw.network.models.PetGetModel;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class DogListAdapter extends ArrayAdapter<PetGetModel> {
    public DogListAdapter(Context context, ArrayList<PetGetModel> pets) {
        super(context, 0, pets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        PetGetModel pet = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_dog_list, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.dog_name);
        TextView tvHome = (TextView) convertView.findViewById(R.id.dog_age_weight);
        ImageView picture = (ImageView) convertView.findViewById(R.id.dog_image);

        picture.setImageDrawable(LoadImageFromWebOperations(pet.getPicture()));
        tvName.setText(pet.getPetID());
        String pet_age_weight = pet.getAge() + " / " + pet.getWeight();
        tvHome.setText(pet_age_weight);

        return convertView;
    }

    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "Dog Image");
            return d;
        } catch (Exception e) {
            return null;
        }
    }
}

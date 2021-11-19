package com.butterflies.stepaw;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.butterflies.stepaw.network.models.PetGetModel;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class DogListAdapter extends BaseAdapter {
    ArrayList<PetGetModel> petList;
    Context context;

    public DogListAdapter(Context context, ArrayList<PetGetModel> pets) {
        this.petList = pets;
        this.context = context;
    }

    @Override
    public int getCount() {
        return petList.size();
    }

    @Override
    public PetGetModel getItem(int i) {
        return petList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView1, ViewGroup parent) {
        // Get the data item for this position

        PetGetModel pet = getItem(position);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.dog_list_item, parent, false);

        TextView tvName = (TextView) row.findViewById(R.id.dog_name);
        TextView tvHome = (TextView) row.findViewById(R.id.dog_age_weight);
        ImageView picture = (ImageView) row.findViewById(R.id.dog_image);
        ImageView beacon = (ImageView) row.findViewById(R.id.beacon);
        ImageView editPet = (ImageView) row.findViewById(R.id.editPet);
        ImageView petWalkBtn = (ImageView) row.findViewById(R.id.petWalkBtn);
        ImageView bluetooth = (ImageView) row.findViewById(R.id.bluetooth);

        Glide.with(context).load("https://images.dog.ceo/breeds/shiba/shiba-15.jpg").into(picture);

        tvName.setText(pet.getPetID());
        String pet_age_weight = pet.getAge() + " / " + pet.getWeight();
        tvHome.setText(pet_age_weight);

        beacon.setImageResource(R.drawable.ic_frame);
        editPet.setImageResource(R.drawable.ic_edit_icon);
        bluetooth.setImageResource(R.drawable.ic_bluetooth);

        petWalkBtn.setImageResource(R.drawable.ic_walk_paw);
        petWalkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChartReport.class);
                intent.putExtra("petId", pet.getPetID());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(intent);
            }
        });

        return row;
    }
}

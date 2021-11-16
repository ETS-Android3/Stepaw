package com.butterflies.stepaw;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;

import com.butterflies.stepaw.network.ApiService;
import com.butterflies.stepaw.network.models.PetGetModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import kotlin.NotImplementedError;
import kotlin.jvm.internal.Intrinsics;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DogList extends AppCompatActivity {

    private  Retrofit retrofit;
    private  ApiService service;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_list);
        String token = null;
        SharedPreferences pref = getSharedPreferences("com.butterflies.stepaw", Context.MODE_PRIVATE);
        token = pref.getString("com.butterflies.stepaw.idToken", "invalid");
        System.out.println("Token " + token );
        if (token != null) {
            getAllPets(token);
        }

    }


    public final void getAllPets(@NotNull String token) {
        Intrinsics.checkNotNullParameter(token, "token");
        Call pets = this.service.getAllPets(" Bearer " + token);
        pets.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Object pets = response.body();
                ArrayList<PetGetModel> petList = (ArrayList<PetGetModel>) response.body();

                DogListAdapter adapter = new DogListAdapter(getApplicationContext(), petList);
//                ListView listView = (ListView) findViewById(R.id.dog_list);
//                listView.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }
}
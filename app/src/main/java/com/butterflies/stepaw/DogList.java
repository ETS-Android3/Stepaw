package com.butterflies.stepaw;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.butterflies.stepaw.dogonboarding.OnBoardingHost;
import com.butterflies.stepaw.network.ApiService;
import com.butterflies.stepaw.network.models.PetGetModel;
import com.butterflies.stepaw.network.models.PetModel;
import com.butterflies.stepaw.scanner.BleConnectionScreen;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import kotlin.NotImplementedError;
import kotlin.jvm.internal.Intrinsics;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class DogList extends AppCompatActivity {

    private  Retrofit retrofit;
    private  ApiService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_list);
        retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build();
        service = retrofit.create(ApiService.class);
        String token = "ya29.a0ARrdaM_u23mjkQ1IUdyYgvzgbOGHYnaXEBCnSNgimBn9r_oP2u6QS7F3uNDYD83guUwHTHuhYxuydOQkJS4gJeqo-6Z_QbuKW8BQaBv1dzhPRTDE0fcy8Zr73JNf3F4uuVIQuuw2DpzowYDJlB-LayFmMskJ";
        SharedPreferences pref = getSharedPreferences("com.butterflies.stepaw", Context.MODE_PRIVATE);
        token = pref.getString("com.butterflies.stepaw.idToken", "invalid");
        System.out.println("Token " + token );
        if (token != null) {
            getAllPets(token);
        }

        Button addDog = findViewById(R.id.addDog);
        addDog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OnBoardingHost.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
        System.exit(1);
    }

    public final void getAllPets(@NotNull String token) {
        Intrinsics.checkNotNullParameter(token, "token");
        Call pets = this.service.getAllPets(" Bearer " + token);
        pets.enqueue(new Callback() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call call, Response response) {
                Object pets = response.body();
                ArrayList<String> distinctPetNames = new ArrayList<>();
                ArrayList<PetGetModel> distinctPetList = new ArrayList<>();
                ArrayList<PetGetModel> petList = (ArrayList<PetGetModel>) response.body();

                for (int i = 0; i < petList.size(); i++) {
                    if(!distinctPetNames.contains(petList.get(i).getPetName())){
                        distinctPetNames.add(petList.get(i).getPetName());
                        distinctPetList.add(petList.get(i));
                    }
                }

                System.out.println(distinctPetList);

                ListView listView = (ListView) findViewById(R.id.dog_list);
                DogListAdapter adapter = new DogListAdapter(getApplicationContext(), distinctPetList);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        PetGetModel item = (PetGetModel) listView.getItemAtPosition(position);
                        Intent intent = new Intent(getApplicationContext(), BleConnectionScreen.class);
                        //intent.putExtra("petId", item.getPetID());
                        intent.putExtra("petId", item.getUserID());
                        intent.putExtra("petName", item.getPetName());
                        startActivity(intent);

                    }
                });

            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }
}
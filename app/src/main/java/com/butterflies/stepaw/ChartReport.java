package com.butterflies.stepaw;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.FragmentKt;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.butterflies.stepaw.authentication.AuthUIHost;
import com.butterflies.stepaw.databinding.ActivityChartReportBinding;
import com.butterflies.stepaw.network.ApiService;
import com.butterflies.stepaw.network.RetrofitObservable;
import com.butterflies.stepaw.network.models.PetGetModel;
import com.butterflies.stepaw.network.networkCall.NetworkCall;
import com.butterflies.stepaw.reminder.FragmentReminder;
import com.butterflies.stepaw.reminder.NotificationPublisher;
import com.butterflies.stepaw.userActions.Account;
import com.butterflies.stepaw.userActions.Contactus;
import com.butterflies.stepaw.userActions.Notifications;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

import kotlin.jvm.internal.Intrinsics;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class ChartReport extends AppCompatActivity implements FragmentReminder.ReminderService, Observer, NavigationView.OnNavigationItemSelectedListener {
    String timeText;
    PetGetModel petObj;
    private Retrofit retrofit;
    private ApiService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityChartReportBinding binding = ActivityChartReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(findViewById(R.id.my_toolbar));
//        Drawer Toggle
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer,
                findViewById(R.id.my_toolbar),
                R.string.nav_open_drawer,
                R.string.nav_close_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//

        Intent intent = getIntent();
        String petId = intent.getStringExtra("petId");

        retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build();
        service = retrofit.create(ApiService.class);
        String token = "ya29.a0ARrdaM8PdCTtbVE9qEKBu1MgiZaIOPPGY4ePq2CVJL3Y3DQUpdWxx14tI-QL1GC3j5_k8OVKbROEQJPkBMQRRqXPWjhsc3j5SLAY47840-MwSX1o3L9c8HZn4bT93l7qCoCdZpFE-u-8ONwM1DEHtuU_uISi";
        SharedPreferences pref = getSharedPreferences("com.butterflies.stepaw", Context.MODE_PRIVATE);
        token = pref.getString("com.butterflies.stepaw.idToken", "invalid");
        System.out.println("Token " + token );
        if (token != null) {
            getPetById(token, petId);
        }

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;

        sectionsPagerAdapter.addFragment(new DailyFragment(), "Day");
        sectionsPagerAdapter.addFragment(new WeeklyFragment(), "Week");
        sectionsPagerAdapter.addFragment(new MonthlyFragment(), "Month");
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabs = binding.chartTabLayout;
        tabs.setupWithViewPager(viewPager);


        Fragment fr = this.getSupportFragmentManager().findFragmentById(R.id.nav_host);
        if (fr == null) {
            throw new NullPointerException("null cannot be cast to non-null type androidx.navigation.fragment.NavHostFragment");
        } else {
            NavHostFragment navHostFragment = (NavHostFragment) fr;
            FragmentKt.findNavController((Fragment) navHostFragment).setGraph(R.navigation.reminder_nav);
        }
//Handling bottom sheet
        BottomSheetBehavior<View> standardBottomSheetBehavior =
                BottomSheetBehavior.from(findViewById(R.id.bottom_sheet_reminder));

        standardBottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                float half_height = (float) 0.73;

            }
        });
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //Code to handle navigation clicks
        switch (item.getItemId()) {
            case R.id.account:
                Intent intent = new Intent(this, Account.class);
                startActivity(intent);
                break;
            case R.id.notifications:
                Intent notification = new Intent(this, Notifications.class);
                startActivity(notification);
                break;

            case R.id.contactus:
                Intent contact = new Intent(this, Contactus.class);
                startActivity(contact);
                break;

            default:


        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
        System.exit(1);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    public void setReminder(@NonNull String hour, @NonNull String minute, @NonNull int... days) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
        calendar.set(Calendar.MINUTE, Integer.parseInt(minute));
        calendar.set(Calendar.SECOND, 0);
        updateTimeText(calendar);
        startAlarm(calendar);
    }

    private void updateTimeText(Calendar c) {
        timeText = "Alarm set for: ";
        timeText += DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
    }

    private void startAlarm(Calendar c) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationPublisher.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }
        Objects.requireNonNull(alarmManager).setInexactRepeating(AlarmManager.RTC_WAKEUP, AlarmManager.INTERVAL_DAY * 7,
                c.getTimeInMillis(), pendingIntent);
    }

    //Implementing observable interface for update of data from network callback
    @Override
    public void update(Observable o, Object arg) {

    }

    @Override
    protected void onResume() {
        RetrofitObservable r = new RetrofitObservable();
        r.getInstance().addObserver(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        RetrofitObservable r = new RetrofitObservable();
        r.deleteObserver(this);
        super.onPause();
    }

    public final void getPetById(@NotNull String token, @NonNull String petId) {
        Intrinsics.checkNotNullParameter(token, "token");
        Call pet = this.service.getPetById(" Bearer " + token, petId);
        pet.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                petObj = (PetGetModel) response.body();
                TextView petName = findViewById(R.id.petName);
                ImageView petImage = findViewById(R.id.petImage);
                TextView petAge = findViewById(R.id.petAge);

                petName.setText(petObj.getPetID());
                String petAgeWeight = petObj.getAge() + " / " + petObj.getWeight();
                petAge.setText(petAgeWeight);

                Glide.with(getApplicationContext()).load("https://images.dog.ceo/breeds/shiba/shiba-15.jpg").into(petImage);
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }
}


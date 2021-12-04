package com.butterflies.stepaw;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.FragmentKt;
import androidx.navigation.fragment.NavHostFragment;
import androidx.room.Room;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.butterflies.stepaw.authentication.AuthUIHost;
import com.butterflies.stepaw.ble.BluetoothLeService;
import com.butterflies.stepaw.databinding.ActivityChartReportBinding;
import com.butterflies.stepaw.network.ApiService;
import com.butterflies.stepaw.network.RetrofitObservable;
import com.butterflies.stepaw.network.models.PetGetModel;
import com.butterflies.stepaw.reminder.FragmentReminder;
import com.butterflies.stepaw.reminder.NotificationPublisher;
import com.butterflies.stepaw.roomORM.ReminderDB;
import com.butterflies.stepaw.roomORM.ReminderDao;
import com.butterflies.stepaw.roomORM.ReminderEntity;
import com.butterflies.stepaw.userActions.AccountActivity;
import com.butterflies.stepaw.userActions.ContactUsActivity;
import com.butterflies.stepaw.userActions.NotificationsActivity;
import com.butterflies.stepaw.utils.StepawUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
    ActivityChartReportBinding binding;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawer;
    private String deviceAddress = "";
    private String petId = "";
    private Boolean connected = null;
    private BluetoothLeService bluetoothService = null;
    private String token, petName;
    private final Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChartReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
//        Drawer Toggle
        drawer = binding.drawerLayout;
        toggle = new ActionBarDrawerToggle(this,
                drawer,
                findViewById(R.id.my_toolbar),
                R.string.nav_open_drawer,
                R.string.nav_close_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setHomeAsUpIndicator(R.drawable.custom_back_button);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ChartReport.super.onBackPressed();
//                }
            }


        });


//        Change hamburger icon color to black
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.black, this.getTheme()));

//
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        Handle logout
        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(ChartReport.this, AuthUIHost.class);
                startActivity(i);
            }
        });

        Intent intent = getIntent();
//        String petId = intent.getStringExtra("petId");
        petId = "BsFHEoXIBEgJKXKVSJWU7MYriEo1";

        petName = intent.getStringExtra("petName");


        deviceAddress = intent.getStringExtra("address");
        retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build();
        service = retrofit.create(ApiService.class);
        token = "ya29.a0ARrdaM_u23mjkQ1IUdyYgvzgbOGHYnaXEBCnSNgimBn9r_oP2u6QS7F3uNDYD83guUwHTHuhYxuydOQkJS4gJeqo-6Z_QbuKW8BQaBv1dzhPRTDE0fcy8Zr73JNf3F4uuVIQuuw2DpzowYDJlB-LayFmMskJ";
        SharedPreferences pref = getSharedPreferences("com.butterflies.stepaw", Context.MODE_PRIVATE);
        token = pref.getString("com.butterflies.stepaw.idToken", "invalid");
        if (token != null) {
            getPetById(token, petId, 0, 0L);
        }
//        if(!isMyServiceRunning(BluetoothLeService.class)) {
//            Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
//            bindService(gattServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
//        }
//        registerReceiver(gattUpdateReceiver, makeGattUpdateIntentFilter());
//        if (bluetoothService != null) {
//            final boolean result = bluetoothService.connect(deviceAddress);
//            Log.d("TAG", "Connect request result=" + result);
//        }

//Setting nav graph for bottom sheet reminder programatically
        Fragment fr = this.getSupportFragmentManager().findFragmentById(R.id.nav_host);
        if (fr == null) {
            throw new NullPointerException("null cannot be cast to non-null type androidx.navigation.fragment.NavHostFragment");
        } else {
            NavHostFragment navHostFragment = (NavHostFragment) fr;
            FragmentKt.findNavController(navHostFragment).setGraph(R.navigation.reminder_nav);
        }
//

//Handling bottom sheet
        BottomSheetBehavior<View> standardBottomSheetBehavior =
                BottomSheetBehavior.from(findViewById(R.id.bottom_sheet_reminder));
        standardBottomSheetBehavior.setPeekHeight(170);
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
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        switch (item.getItemId()) {
            case R.id.close_nav_icon:
                drawer.closeDrawer(Gravity.RIGHT);
                break;

            case R.id.account:
                Intent intent = new Intent(this, AccountActivity.class);
                startActivity(intent);
                break;
            case R.id.notifications:
                Intent notification = new Intent(this, NotificationsActivity.class);
                startActivity(notification);
                break;

            case R.id.contactus:
                Intent contact = new Intent(this, ContactUsActivity.class);
                startActivity(contact);
                break;

            default:


        }
        drawer.closeDrawer(Gravity.RIGHT);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, DogList.class);
        startActivity(i);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    public void setReminder(@NonNull String hour, @NonNull String minute, @NonNull String label, @NonNull int... days) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
        calendar.set(Calendar.MINUTE, Integer.parseInt(minute));
        calendar.set(Calendar.SECOND, 0);
//        calendar.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
//        calendar.set(Calendar.DAY_OF_WEEK,Calendar.FRIDAY);
//        calendar.set(Calendar.DAY_OF_WEEK,Calendar.THURSDAY);
//        calendar.set(Calendar.DAY_OF_WEEK,Calendar.WEDNESDAY);
//        calendar.set(Calendar.DAY_OF_WEEK,Calendar.TUESDAY);
//        calendar.set(Calendar.DAY_OF_WEEK,Calendar.SATURDAY);
//        calendar.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);

        StepawUtils a = new StepawUtils();

      ReminderDB db = Room.databaseBuilder(this,
                ReminderDB.class, "remindersDB").build();
        int unique = a.getUniqueID();
        ReminderDao rdao = db.reminderdao();

//        Insert reminder data into sqlite db
        AsyncTask.execute(() -> {
            rdao.insertAll(new ReminderEntity(unique, hour, minute, label));
            Log.d("reminders",rdao.getById(unique).getMinute());
        });
//

        updateTimeText(calendar);
        startAlarm(calendar, label, unique);
    }

    private void updateTimeText(Calendar c) {
        timeText = "Alarm set for: ";
        timeText += DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
    }

    private void startAlarm(Calendar c, String label, int uniqueCode) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationPublisher.class);
        intent.putExtra("reminderlabel", label);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, uniqueCode, intent, 0);
        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }
//        Objects.requireNonNull(alarmManager).setRepeating(AlarmManager.RTC_WAKEUP, AlarmManager.INTERVAL_DAY ,
//                c.getTimeInMillis(), pendingIntent);

        Objects.requireNonNull(alarmManager).setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    //Implementing observable interface for update of data from network callback
    @Override
    public void update(Observable o, Object arg) {

    }

    //Handle menu click
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_drawer, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void openNavDrawer(MenuItem item) {
        if (drawer.isDrawerOpen(Gravity.RIGHT)) {
            drawer.closeDrawer(Gravity.RIGHT);
        } else {
            drawer.openDrawer(Gravity.RIGHT);
        }
    }

    //
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
//        unregisterReceiver(gattUpdateReceiver);
    }

    public final void getPetById(@NotNull String token, @NonNull String id, int step, Long distance) {
        Intrinsics.checkNotNullParameter(token, "token");
        Call<List<PetGetModel>> pets = this.service.getPetById(" Bearer " + token, id);
        pets.enqueue(new Callback() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call wcall, Response response) {
//            petObj = (PetGetModel) response.body()
                //weekly chart variables
                Log.d("pets",response.message());
                ArrayList<PetGetModel> weekArray = new ArrayList<>();
                ArrayList<String> daysArray = new ArrayList<>(
                        Arrays.asList("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"));
                String[] days = new String[7];

                double[] weekDistance = new double[7];
                double[] weekTime = new double[7];
                int[] weekSteps = new int[7];
                String[] weekDates = new String[7];

                //monthly chart variables
                ArrayList<PetGetModel> month1 = new ArrayList<>();
                ArrayList<PetGetModel> month2 = new ArrayList<>();
                ArrayList<PetGetModel> month3 = new ArrayList<>();
                ArrayList<PetGetModel> month4 = new ArrayList<>();
                ArrayList<PetGetModel> month5 = new ArrayList<>();
                ArrayList<PetGetModel> month6 = new ArrayList<>();
                double[] distance = new double[6];
                double[] time = new double[6];
                int[] steps = new int[6];
                String[] months = new String[6];
                ArrayList<String> monthArray = new ArrayList<>(Arrays.asList("Jan", "Feb", "Mar", "Apr", "May",
                        "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"));
                Object obj = response.body();
                System.out.println("Dog data");
                System.out.println(obj);
                ArrayList<PetGetModel> petList = (ArrayList<PetGetModel>) response.body();
                TextView petNameTxtView = findViewById(R.id.petName);
                ImageView petImage = findViewById(R.id.petImage);
                TextView petAge = findViewById(R.id.petAge);

                petList = (ArrayList<PetGetModel>) petList.stream().filter(x -> x.getPetName().equals(petName));

                if (petList != null) {
                    petObj = petList.get(0);

                    try {
                        Calendar cal = Calendar.getInstance();
                        Date today = new Date();
                        cal.setTime(today);

                        cal.add(Calendar.DATE, -7);
                        Date week = cal.getTime();
                        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
                        Date today30 = cal.getTime();

                        cal.set(Calendar.MONTH, today30.getMonth() - 1);
                        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
                        Date today60 = cal.getTime();

                        cal.set(Calendar.MONTH, today60.getMonth() - 1);
                        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
                        Date today90 = cal.getTime();

                        cal.set(Calendar.MONTH, today90.getMonth() - 1);
                        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
                        Date today120 = cal.getTime();

                        cal.set(Calendar.MONTH, today120.getMonth() - 1);
                        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
                        Date today150 = cal.getTime();

                        cal.set(Calendar.MONTH, today150.getMonth() - 1);
                        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
                        Date today180 = cal.getTime();

                        for (int k = 0; k < 7; k++) {
                            cal = Calendar.getInstance();
                            cal.add(Calendar.DATE, -k);
                            Date weekDays = cal.getTime();
                            days[k] = daysArray.get(weekDays.getDay());
                        }

                        for (int i = 0; i < petList.size(); i++) {
                            Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").
                                    parse(petList.get(i).getDate());

                            //filter last week data //set weekly data array
                            assert date != null;
                            if (date.compareTo(week) >= 0 && date.compareTo(today) <= 0) {
                                weekArray.add(petList.get(i));
                                weekDistance[i] = Double.parseDouble(petList.get(i).getDistance());
                                weekSteps[i] = Integer.parseInt(petList.get(i).getNumberOfSteps());
                                weekTime[i] = Double.parseDouble(petList.get(i).getDuration());
//                                days[i] = daysArray.get(date.getDay());
                                SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy");
                                weekDates[i] = formatter.format(date);

                            } else if (i <= 6) {
                                weekDistance[i] = 0;
                                weekSteps[i] = 0;
                                weekTime[i] = 0;
//                                days[i] = daysArray.get(date.getDay());
                                SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy");
                                weekDates[i] = formatter.format(date);
                            }

                            //filter data based on month
                            if (date.compareTo(today30) >= 0 && date.compareTo(today) <= 0) {
                                month1.add(petList.get(i));
                                int monthCounter = date.getMonth();
                                if (months[0] == null) {
                                    for (int j = 0; j < 6; j++) {
                                        if (monthCounter >= 0) {
                                            months[j] = monthArray.get(monthCounter);
                                            monthCounter--;
                                        } else {
                                            monthCounter = 11;
                                            months[j] = monthArray.get(monthCounter);
                                            monthCounter--;
                                        }
                                    }
                                }
                            } else if (date.compareTo(today60) >= 0 && date.compareTo(today30) < 0) {
                                month2.add(petList.get(i));
                            } else if (date.compareTo(today90) >= 0 && date.compareTo(today60) < 0) {
                                month3.add(petList.get(i));
                            } else if (date.compareTo(today120) >= 0 && date.compareTo(today90) <= 0) {
                                month4.add(petList.get(i));
                            } else if (date.compareTo(today150) >= 0 && date.compareTo(today120) < 0) {
                                month5.add(petList.get(i));
                            } else if (date.compareTo(today180) >= 0 && date.compareTo(today150) < 0) {
                                month6.add(petList.get(i));
                            }
                        }
//                        System.out.println("Week Array " + weekArray);
//                        System.out.println("Last 1 month Array " + month1);
//                        System.out.println("Last 2 month Array " + month2);
//                        System.out.println("Last 3 month Array " + month3);
//                        System.out.println("Last 4 month Array " + month4);
//                        System.out.println("Last 5 month Array " + month5);
//                        System.out.println("Last 6 month Array " + month6);

                        double distanceSum, timeSum;
                        int stepSum;
                        distanceSum = month1.stream().mapToDouble(p -> Float.parseFloat(p.getDistance())).sum();
                        timeSum = month1.stream().mapToDouble(p -> Float.parseFloat(p.getDuration())).sum();
                        stepSum = month1.stream().mapToInt(p -> Integer.parseInt(p.getNumberOfSteps())).sum();
                        distance[0] = distanceSum;
                        time[0] = timeSum;
                        steps[0] = stepSum;

                        distanceSum = month2.stream().mapToDouble(p -> Float.parseFloat(p.getDistance())).sum();
                        timeSum = month2.stream().mapToDouble(p -> Float.parseFloat(p.getDuration())).sum();
                        stepSum = month2.stream().mapToInt(p -> Integer.parseInt(p.getNumberOfSteps())).sum();
                        distance[1] = distanceSum;
                        time[1] = timeSum;
                        steps[1] = stepSum;

                        distanceSum = month3.stream().mapToDouble(p -> Float.parseFloat(p.getDistance())).sum();
                        timeSum = month3.stream().mapToDouble(p -> Float.parseFloat(p.getDuration())).sum();
                        stepSum = month3.stream().mapToInt(p -> Integer.parseInt(p.getNumberOfSteps())).sum();
                        distance[2] = distanceSum;
                        time[2] = timeSum;
                        steps[2] = stepSum;

                        distanceSum = month4.stream().mapToDouble(p -> Float.parseFloat(p.getDistance())).sum();
                        timeSum = month4.stream().mapToDouble(p -> Float.parseFloat(p.getDuration())).sum();
                        stepSum = month4.stream().mapToInt(p -> Integer.parseInt(p.getNumberOfSteps())).sum();
                        distance[3] = distanceSum;
                        time[3] = timeSum;
                        steps[3] = stepSum;

                        distanceSum = month5.stream().mapToDouble(p -> Float.parseFloat(p.getDistance())).sum();
                        timeSum = month5.stream().mapToDouble(p -> Float.parseFloat(p.getDuration())).sum();
                        stepSum = month5.stream().mapToInt(p -> Integer.parseInt(p.getNumberOfSteps())).sum();
                        distance[4] = distanceSum;
                        time[4] = timeSum;
                        steps[4] = stepSum;

                        distanceSum = month6.stream().mapToDouble(p -> Float.parseFloat(p.getDistance())).sum();
                        timeSum = month6.stream().mapToDouble(p -> Float.parseFloat(p.getDuration())).sum();
                        stepSum = month6.stream().mapToInt(p -> Integer.parseInt(p.getNumberOfSteps())).sum();
                        distance[5] = distanceSum;
                        time[5] = timeSum;
                        steps[5] = stepSum;

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                if (petObj != null) {

                    if (!petObj.getPetName().equals("")) {
                        petNameTxtView.setText(petObj.getPetName());
                        String petAgeWeight = petObj.getAge() + "y / " + petObj.getWeight() + " kg";
                        petAge.setText(petAgeWeight);

                        if (petObj.getPicture() != null) {
                            Glide.with(getApplicationContext()).load(petObj.getPicture()).into(petImage);
                        } else {
                            Glide.with(getApplicationContext()).load("https://images.dog.ceo/breeds/shiba/shiba-15.jpg").into(petImage);
                        }

                        Bundle bundle = new Bundle();
                        bundle.putString("petId", petObj.getUserID());
                        bundle.putString("petKm", petObj.getDistance());
                        bundle.putString("petMin", petObj.getDuration());
                        bundle.putString("petSteps", petObj.getNumberOfSteps());
                        float kmCap = Float.parseFloat(String.valueOf(Arrays.stream(weekDistance).average().orElse(0.0)));
                        float minCap = Float.parseFloat(String.valueOf(Arrays.stream(weekTime).average().orElse(0.0)));
                        bundle.putFloat("petKmCap", Float.parseFloat(String.valueOf(kmCap)));
                        bundle.putFloat("petMinCap", Float.parseFloat(String.valueOf(minCap)));
                        bundle.putString("date", petObj.getDate());

                        DailyFragment dailyFragment = new DailyFragment();
                        dailyFragment.setArguments(bundle);

                        bundle = new Bundle();
                        bundle.putDoubleArray("petKmArray", weekDistance);
                        bundle.putDoubleArray("petMinArray", weekTime);
                        bundle.putIntArray("petStepsArray", weekSteps);
                        bundle.putStringArray("days", days);
                        bundle.putStringArray("dateArray", weekDates);
                        WeeklyFragment weeklyFragment = new WeeklyFragment();
                        weeklyFragment.setArguments(bundle);

                        bundle = new Bundle();
                        bundle.putDoubleArray("petKmArray", distance);
                        bundle.putDoubleArray("petMinArray", time);
                        bundle.putIntArray("petStepsArray", steps);
                        bundle.putStringArray("monthArray", months);
                        MonthlyFragment monthlyFragment = new MonthlyFragment();
                        monthlyFragment.setArguments(bundle);

                        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
                        ViewPager viewPager = binding.viewPager;

                        sectionsPagerAdapter.addFragment(dailyFragment, "Day");
                        sectionsPagerAdapter.addFragment(weeklyFragment, "Week");
                        sectionsPagerAdapter.addFragment(monthlyFragment, "Month");
                        viewPager.setAdapter(sectionsPagerAdapter);

                        TabLayout tabs = binding.chartTabLayout;
                        tabs.setupWithViewPager(viewPager);
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e("pets", t.getMessage());
            }
        });
    }

}


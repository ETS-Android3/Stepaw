package com.butterflies.stepaw;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.FragmentKt;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager.widget.ViewPager;

import com.butterflies.stepaw.databinding.ActivityChartReportBinding;
import com.butterflies.stepaw.reminder.FragmentReminder;
import com.butterflies.stepaw.reminder.NotificationPublisher;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.tabs.TabLayout;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Objects;

public class ChartReport extends AppCompatActivity implements FragmentReminder.ReminderService {
String timeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityChartReportBinding binding = ActivityChartReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
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


    }

    @Override
    public void setReminder(@NonNull String hour, @NonNull String minute) {
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
        Objects.requireNonNull(alarmManager).setExact(AlarmManager.RTC_WAKEUP,
                c.getTimeInMillis(), pendingIntent);
    }
}

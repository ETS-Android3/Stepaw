package com.butterflies.stepaw;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;

import com.butterflies.stepaw.databinding.ActivityChartReportBinding;
import com.google.android.material.tabs.TabLayout;

public class ChartReport extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityChartReportBinding binding = ActivityChartReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(findViewById(R.id.my_toolbar));
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;

        sectionsPagerAdapter.addFragment(new DailyFragment(), "Day");
        sectionsPagerAdapter.addFragment(new WeeklyFragment(), "Week");
        sectionsPagerAdapter.addFragment(new MonthlyFragment(), "Month");
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabs = binding.chartTabLayout;
        tabs.setupWithViewPager(viewPager);
   }



}

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

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.chartTabLayout;
        tabs.setupWithViewPager(viewPager);
   }
}

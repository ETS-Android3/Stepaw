package com.butterflies.stepaw;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ChartAdapter extends FragmentStateAdapter {

    int totalTabs = 3;

    public ChartAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle ) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return WeeklyFragment.newInstance("", "");
//                WeeklyFragment weeklyFragment = new WeeklyFragment();
//                return weeklyFragment;

            case 1:
                DailyFragment dailyFragment = new DailyFragment();
                return dailyFragment;
            case 2:
                MonthlyFragment monthlyFragment = new MonthlyFragment();
                return monthlyFragment;
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return totalTabs;
    }
}

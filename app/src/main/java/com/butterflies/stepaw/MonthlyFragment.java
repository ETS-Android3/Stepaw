package com.butterflies.stepaw;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import app.futured.donut.DonutProgressView;
import app.futured.donut.DonutSection;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MonthlyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MonthlyFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    BarChart barChart;
    private List<DonutSection> kmDataList = new ArrayList<>();
    private List<DonutSection> minDataList = new ArrayList<>();

    public MonthlyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MonthlyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MonthlyFragment newInstance(String param1, String param2) {
        MonthlyFragment fragment = new MonthlyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_monthly, container, false);

        //generate monthly report - bar chart
        barChart = view.findViewById(R.id.barChart);
        barChart.setTouchEnabled(true);
        barChart.setDrawBarShadow(false);
        barChart.setPinchZoom(true);

        assert getArguments() != null;
        double[] distanceArray = getArguments().getDoubleArray("petKmArray");
        double[] timeArray = getArguments().getDoubleArray("petMinArray");
        int[] stepsArray = getArguments().getIntArray("petStepsArray");
        String[] monthArray = getArguments().getStringArray("monthArray");
        Calendar cal = Calendar.getInstance();

        TextView stepCount = view.findViewById(R.id.stepsCountTextView);
        TextView minValue = view.findViewById(R.id.minValue);
        TextView kmValue = view.findViewById(R.id.kmValue);
        TextView dateTextView = view.findViewById(R.id.dateTextView);

        showBarChart(stepsArray);
        initBarChart(monthArray);

        //generate daily report - donut chart
        //daily report chart generator - donut chart
        DonutProgressView kmDonutChart = view.findViewById(R.id.kmDonutView);
        DonutProgressView minDonutChart = view.findViewById(R.id.minDonutView);

        float kmCap = Float.parseFloat(String.valueOf(Arrays.stream(distanceArray).average().orElse(0.0)));
        float minCap = Float.parseFloat(String.valueOf(Arrays.stream(timeArray).average().orElse(0.0)));


        renderDonutData(kmDonutChart, minDonutChart, distanceArray[0], timeArray[0], kmCap, minCap);

        stepCount.setText(String.valueOf(stepsArray[0]));
        minValue.setText(String.valueOf( timeArray[0]));
        kmValue.setText(String.valueOf(distanceArray[0]));
        dateTextView.setText(new StringBuilder().append(monthArray[0]).append(" ").append(cal.get(Calendar.YEAR)).toString());

        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                //Toast.makeText(getActivity(), "Value selected", Toast.LENGTH_SHORT).show();
                int index = (int) e.getX();
                renderDonutData(kmDonutChart, minDonutChart, distanceArray[index], timeArray[index],
                        kmCap, minCap);
                stepCount.setText(String.valueOf(stepsArray[index]));
                minValue.setText(String.valueOf( timeArray[index]));
                kmValue.setText(String.valueOf(distanceArray[index]));
                dateTextView.setText(new StringBuilder().append(monthArray[index]).append(" ").append(cal.get(Calendar.YEAR)).toString());
            }

            @Override
            public void onNothingSelected() {

            }
        });

        return view;
    }

    private void showBarChart(int[] stepsArray){
        ArrayList<BarEntry> entries = new ArrayList<>();

        //fit the data into a bar
        for (int i = 0; i < stepsArray.length; i++) {
            BarEntry barEntry = new BarEntry(i, stepsArray[i]);
            entries.add(barEntry);
        }

        BarDataSet barDataSet = new BarDataSet(entries, "");
        barDataSet.setDrawValues(false);
        barDataSet.setColor(Color.parseColor("#DFE9F8"));
        barDataSet.setBarShadowColor(Color.rgb(255,222,46));
        BarData data = new BarData(barDataSet);
        data.setBarWidth(0.4f);

        barChart.setData(data);
        barChart.invalidate();
    }

    private void initBarChart(String[] monthArray){
        barChart.setDrawGridBackground(false);

        barChart.setDrawBorders(false);

        barChart.animateY(1000);
        barChart.animateX(1000);

        barChart.getAxisLeft().setEnabled(false);
        barChart.getLegend().setEnabled(false);
        barChart.getDescription().setEnabled(false);
        barChart.setHighlightFullBarEnabled(true);
        barChart.setRenderer(new RoundedBarChart(barChart, barChart.getAnimator(), barChart.getViewPortHandler(), 50));

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawLabels(true);
        xAxis.setTextSize(12);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(monthArray));
        xAxis.setGranularity(1f);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setDrawAxisLine(false);
        leftAxis.setDrawLimitLinesBehindData(false);

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setDrawAxisLine(false);
        rightAxis.setDrawLabels(false);
        rightAxis.enableGridDashedLine(10f, 10f, 100f);
    }

    private void renderDonutData(DonutProgressView kmDonutChart, DonutProgressView minDonutChart,
                                 double distance, double time, float kmCap, float minCap){

        DonutSection kmSection = new DonutSection("km",
                Color.parseColor("#004E99"), Float.parseFloat(String.valueOf(distance)));

        DonutSection minSection = new DonutSection("min",
                Color.parseColor("#FBD617"),Float.parseFloat(String.valueOf(time)));

        kmDataList = new ArrayList<>();
        kmDataList.add(kmSection);
        kmDonutChart.setCap(kmCap);
        kmDonutChart.submitData(kmDataList);
        kmDonutChart.animate();

        minDataList = new ArrayList<>();
        minDataList.add(minSection);
        minDonutChart.setCap(minCap);
        minDonutChart.submitData(minDataList);
        minDonutChart.animate();

    }
}
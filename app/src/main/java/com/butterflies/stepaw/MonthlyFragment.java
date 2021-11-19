package com.butterflies.stepaw;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import java.util.List;
import java.util.Random;

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
        showBarChart();
        initBarChart();

        //generate daily report - donut chart
        //daily report chart generator - donut chart
        DonutProgressView kmDonutChart = view.findViewById(R.id.kmDonutView);
        DonutProgressView minDonutChart = view.findViewById(R.id.minDonutView);

        renderDonutData(kmDonutChart, minDonutChart);

        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                //Toast.makeText(getActivity(), "Value selected", Toast.LENGTH_SHORT).show();
                renderDonutData(kmDonutChart, minDonutChart);
                //System.out.println(e.getX()+ " " + e.getY());
            }

            @Override
            public void onNothingSelected() {

            }
        });

        return view;
    }

    private void showBarChart(){
        ArrayList<Double> valueList = new ArrayList<>();

        ArrayList<BarEntry> entries = new ArrayList<>();

        //input data
        for(int i = 0; i <= 6; i++){
            valueList.add(i * 100.1);
        }

        //fit the data into a bar
        for (int i = 0; i < valueList.size(); i++) {
            BarEntry barEntry = new BarEntry(i, valueList.get(i).floatValue());
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

    private ArrayList<String> getXAxisValues()
    {
        ArrayList<String> labels = new ArrayList<> ();

        labels.add( "SUN");
        labels.add( "MON");
        labels.add( "TUE");
        labels.add( "WED");
        labels.add( "THU");
        labels.add( "FRI");
        labels.add( "SAT");
        return labels;
    }

    private void initBarChart(){
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
        xAxis.setValueFormatter(new IndexAxisValueFormatter(getXAxisValues()));
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

    private void renderDonutData(DonutProgressView kmDonutChart, DonutProgressView minDonutChart){

        Random rn = new Random();
        float answer = rn.nextInt(5) + 1;
        DonutSection kmSection = new DonutSection("km",
                Color.parseColor("#004E99"), answer);

        DonutSection minSection = new DonutSection("min",
                Color.parseColor("#FBD617"),answer);

        kmDataList = new ArrayList<>();
        kmDataList.add(kmSection);
        kmDonutChart.setCap(5f);
        kmDonutChart.submitData(kmDataList);
        kmDonutChart.animate();

        minDataList = new ArrayList<>();
        minDataList.add(minSection);
        minDonutChart.setCap(5f);
        minDonutChart.submitData(minDataList);
        minDonutChart.animate();

    }
}
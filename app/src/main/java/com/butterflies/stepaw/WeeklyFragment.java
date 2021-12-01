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

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import app.futured.donut.DonutProgressView;
import app.futured.donut.DonutSection;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WeeklyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeeklyFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private LineChart mChart;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private List<DonutSection> kmDataList = new ArrayList<>();
    private List<DonutSection> minDataList = new ArrayList<>();


    public WeeklyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WeeklyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WeeklyFragment newInstance(String param1, String param2) {
        WeeklyFragment fragment = new WeeklyFragment();
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
        View view =  inflater.inflate(R.layout.fragment_weekly, container, false);

        //daily report chart generator - donut chart
        DonutProgressView kmDonutChart = view.findViewById(R.id.kmDonutView);
        DonutProgressView minDonutChart = view.findViewById(R.id.minDonutView);

        assert getArguments() != null;
        double[] distanceArray = getArguments().getDoubleArray("petKmArray");
        double[] timeArray = getArguments().getDoubleArray("petMinArray");
        int[] stepsArray = getArguments().getIntArray("petStepsArray");
        String[] days = getArguments().getStringArray("days");
        String[] dates = getArguments().getStringArray("dateArray");

        float kmCap = Float.parseFloat(String.valueOf(Arrays.stream(distanceArray).average().orElse(0.0)));
        float minCap = Float.parseFloat(String.valueOf(Arrays.stream(timeArray).average().orElse(0.0)));

        TextView stepCount = view.findViewById(R.id.stepsCountTextView);
        TextView minValue = view.findViewById(R.id.minValue);
        TextView kmValue = view.findViewById(R.id.kmValue);
        TextView dateTextView = view.findViewById(R.id.dateTextView);

        renderDonutData(kmDonutChart, minDonutChart, distanceArray[0], timeArray[0], kmCap, minCap);

        stepCount.setText(String.valueOf(stepsArray[0]));
        minValue.setText(String.valueOf(timeArray[0]));
        kmValue.setText(String.valueOf(distanceArray[0]));
        dateTextView.setText(dates[0]);

        //weekly report - line chart generation
        mChart = view.findViewById(R.id.chart);
        mChart.setTouchEnabled(true);
        mChart.setPinchZoom(true);
        mChart.animateY(1000);
        mChart.animateX(1000);
        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                int index = (int) e.getX();
                renderDonutData(kmDonutChart, minDonutChart, distanceArray[index], timeArray[index],
                        kmCap, minCap);

                stepCount.setText(String.valueOf(stepsArray[index]));
                minValue.setText(String.valueOf( timeArray[index]));
                kmValue.setText(String.valueOf(distanceArray[index]));
                dateTextView.setText(dates[index]);

                System.out.println(e.getX()+ " " + e.getY());
            }

            @Override
            public void onNothingSelected() {

            }
        });

        if(stepsArray != null && days != null){
            renderData(stepsArray, days);
        }
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
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


    public void renderData(int[] stepsArray, String[] days) {

        XAxis xAxis = mChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawLabels(true);
        xAxis.setTextSize(12);
        xAxis.setDrawAxisLine(false);
        if(days != null && days.length > 0)
            xAxis.setValueFormatter(new IndexAxisValueFormatter(days));

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setEnabled(false);
        leftAxis.setDrawZeroLine(false);
        leftAxis.setDrawLimitLinesBehindData(false);

        mChart.getAxisRight().setEnabled(true);
        mChart.getAxisRight().setDrawLabels(false);
        mChart.getAxisRight().setDrawAxisLine(false);
        mChart.getAxisRight().enableGridDashedLine(10f, 10f, 10f);
        mChart.getLegend().setEnabled(false);
        mChart.getDescription().setEnabled(false);
        setData(stepsArray);
    }

    private void setData(int[] stepsArray) {

        ArrayList<Entry> values = new ArrayList<>();
        if(stepsArray != null && stepsArray.length > 0) {
            for (int i = 0; i < stepsArray.length; i++) {
                values.add(new Entry(i, stepsArray[i]));
            }

            LineDataSet set1;
            if (mChart.getData() != null &&
                    mChart.getData().getDataSetCount() > 0) {
                set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
                set1.setValues(values);
                mChart.getData().notifyDataChanged();
                mChart.getData().notifyDataChanged();
                mChart.notifyDataSetChanged();
            } else {

                set1 = new LineDataSet(values, "Walk Data");
                set1.setDrawIcons(false);
                set1.setColor(Color.DKGRAY);
                set1.setCircleColor(Color.DKGRAY);

                set1.setLineWidth(1.5f);
                set1.setCircleRadius(5f);
                set1.setDrawCircleHole(true);
                set1.setDrawValues(false);
                set1.setDrawFilled(false);
                set1.setFormLineWidth(.5f);
                set1.setFormSize(15f);
                set1.setDrawHorizontalHighlightIndicator(true);
                set1.setHighLightColor(Color.rgb(255,222,46));
                set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);

                ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                dataSets.add(set1);
                LineData data = new LineData(dataSets);

                mChart.setData(data);
            }
        }
    }
}
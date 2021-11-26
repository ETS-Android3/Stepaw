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

import java.util.ArrayList;
import java.util.List;
import app.futured.donut.DonutProgressView;
import app.futured.donut.DonutSection;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DailyFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class DailyFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DailyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DailyFragment newInstance(String param1, String param2) {
        DailyFragment fragment = new DailyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public DailyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_daily, container, false);
        DonutProgressView kmDonutChart = view.findViewById(R.id.kmDonutView);
        DonutProgressView minDonutChart = view.findViewById(R.id.minDonutView);

//        ChartReport activity = (ChartReport) getActivity();
        String strKM = getArguments().getString("petKm");
        String strMin = getArguments().getString("petMin");
        String steps = getArguments().getString("petSteps");
        Float km = Float.parseFloat(strKM);
        Float min = Float.parseFloat(strMin);

        DonutSection kmSection = new DonutSection("km",
                Color.parseColor("#004E99"), km);

        DonutSection minSection = new DonutSection("min",
                Color.parseColor("#FBD617"),min);

        List<DonutSection> list = new ArrayList<>();
        list.add(kmSection);
        kmDonutChart.setCap(km+20);
        kmDonutChart.submitData(list);

        list = new ArrayList<>();
        list.add(minSection);
        minDonutChart.setCap(min+2);
        minDonutChart.submitData(list);

        TextView stepCount = view.findViewById(R.id.stepsCountTextView);
        stepCount.setText(steps);

        TextView minValue = view.findViewById(R.id.minValue);
        minValue.setText(strMin);

        TextView kmValue = view.findViewById(R.id.kmValue);
        kmValue.setText(strKM);

        return view;
    }
}
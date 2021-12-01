package com.butterflies.stepaw;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.butterflies.stepaw.ble.BluetoothLeService;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

    private BluetoothLeService bluetoothService = null;
    private String deviceAddress = "D2:08:EB:01:A0:62";
    private Boolean connected;
    private int prevStep = 0;
    private View mView;
    private int finalStep;
    private String finalTime;
    private TextView stepCount;
    private TextView minValue;
    private TextView kmValue;
    private DonutProgressView minDonutChart;
    private DonutSection minSection;
    private List<DonutSection> list;
    private List<DonutSection> kmlist;
    private DecimalFormat df = new DecimalFormat("##.#");


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        if(!isMyServiceRunning(BluetoothLeService.class)) {
            Intent gattServiceIntent = new Intent(DailyFragment.this.getActivity(), BluetoothLeService.class);
            getActivity().bindService(gattServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        }
        getActivity(). registerReceiver(gattUpdateReceiver, makeGattUpdateIntentFilter());
        if (bluetoothService != null) {
            final boolean result = bluetoothService.connect(deviceAddress);
            Log.d("TAG", "Connect request result=" + result);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_daily, container, false);
        DonutProgressView kmDonutChart = view.findViewById(R.id.kmDonutView);
        minDonutChart = view.findViewById(R.id.minDonutView);

//        ChartReport activity = (ChartReport) getActivity();
        String strKM = getArguments().getString("petKm");
        String strMin = getArguments().getString("petMin");
        String steps = getArguments().getString("petSteps");
        Float kmCap = getArguments().getFloat("petKmCap");
        Float minCap = getArguments().getFloat("petMinCap");
        String dateStr = null;
        try {
            SimpleDateFormat formatter =   new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            Date date = formatter.parse(getArguments().getString("date"));

            formatter = new SimpleDateFormat("MMM dd, yyyy");
            dateStr = formatter.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Float km = Float.parseFloat(strKM);
        Float min = Float.parseFloat(strMin);

        DonutSection kmSection = new DonutSection("km",
                Color.parseColor("#004E99"), km);

        DonutSection minSection = new DonutSection("min",
                Color.parseColor("#FBD617"),min);

        kmlist = new ArrayList<>();
        kmlist.add(kmSection);
        kmDonutChart.setCap(kmCap);
        kmDonutChart.submitData(kmlist);

        list = new ArrayList<>();
        list.add(minSection);
        minDonutChart.setCap(minCap);
        minDonutChart.submitData(list);

        stepCount = view.findViewById(R.id.stepsCountTextView);
        stepCount.setText(steps);

        minValue = view.findViewById(R.id.minValue);
        minValue.setText(strMin);

        kmValue = view.findViewById(R.id.kmValue);
        kmValue.setText(km.toString());

        TextView dateTextView = view.findViewById(R.id.dateTextView);
        dateTextView.setText(dateStr);
        this.mView = view;
        return view;
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            BluetoothLeService bluetoothService = ((BluetoothLeService.LocalBinder) service).getService();
            if (bluetoothService != null) {
                if (!bluetoothService.initialize()) {
                    Log.e("TAG", "Unable to initialize Bluetooth");
                    //finish();
                }
                // perform device connection
                if(deviceAddress != null)
                    bluetoothService.connect(deviceAddress);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            BluetoothLeService bluetoothService = null;
        }
    };

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

   BroadcastReceiver gattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                connected = true;
//                updateConnectionState(R.string.connected);
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                connected = false;
//                updateConnectionState(R.string.disconnected);
            }
            else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                int step = Integer.parseInt(intent.getStringExtra("data"));
                if((step - prevStep) > 10) {
                    prevStep = step;
                    Long Longtime = TimeUnit.MILLISECONDS.toSeconds(
                            Long.parseLong(intent.getStringExtra("runtime")));
                    int time = Longtime.intValue();
                    Log.d("stepcount", intent.getStringExtra("data"));
                    Log.d("runtime", String.valueOf(time));
                    test(step, intent.getStringExtra("runtime"));
                }
            }
        }
    };

    private void test(int step, String time) {
        stepCount.setText(String.valueOf(step));
        String minutes = String.valueOf(TimeUnit.MILLISECONDS.toMinutes(
                Long.parseLong(time)));
        float distance = (float) step * (float) 0.005 * (float) 1.6;
        kmValue.setText(String.valueOf(df.format(distance)));
        minValue.setText(minutes);
        Log.d("minutes", minutes);
        if(!list.isEmpty()) {
            list.remove(0);
            DonutSection minSection = new DonutSection("min",
                    Color.parseColor("#FBD617"),Float.parseFloat(time)+1);
            list.add(minSection);
            minDonutChart.setCap(3f);
            minDonutChart.submitData(list);
        }
//        minDonutChart.addAmount(Float.parseFloat(time));
//        minDonutChart.submitData(list);
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(gattUpdateReceiver);
    }
}
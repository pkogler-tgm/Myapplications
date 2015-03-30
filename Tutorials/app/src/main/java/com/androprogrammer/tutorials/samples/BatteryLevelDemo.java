package com.androprogrammer.tutorials.samples;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.transition.Slide;
import android.transition.Transition;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.androprogrammer.tutorials.R;
import com.androprogrammer.tutorials.activities.Baseactivity;

public class BatteryLevelDemo extends Baseactivity {

    protected View view;
    protected TextView batteryPercent, ChargingState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

            //set the transition
            Transition ts = new Slide();
            ts.setDuration(5000);
            getWindow().setEnterTransition(ts);
            getWindow().setExitTransition(ts);
        } else {
            //listViewCarrier.setIndicatorBoundsRelative(500, 0);
        }

        super.onCreate(savedInstanceState);

        setReference();

        setToolbarTittle(this.getClass().getSimpleName());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getBatteryPercentage();

        if(isPhonePluggedIn(BatteryLevelDemo.this).compareToIgnoreCase("yes") == 0)
        {
            ChargingState.setText("yes");
        }
        else {
            ChargingState.setText("no");
        }

    }

    @Override
    public void setReference() {

        view = LayoutInflater.from(this).inflate(R.layout.activity_batterylevel_demo,container);

        batteryPercent = (TextView) view.findViewById(R.id.tv_percentage);
        ChargingState = (TextView) view.findViewById(R.id.tv_state);
    }


    private void getBatteryPercentage() {

        BroadcastReceiver batteryLevel = new BroadcastReceiver() {

            public void onReceive(Context context, Intent intent)
            {
                context.unregisterReceiver(this);
                int currentLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                int level= -1;
                if (currentLevel >= 0 && scale > 0) {
                    level = (currentLevel * 100) / scale;
                }
                batteryPercent.setText(level + "%");
            }
        };

        IntentFilter batteryLevelFilter = new IntentFilter(
                Intent.ACTION_BATTERY_CHANGED);

        registerReceiver(batteryLevel, batteryLevelFilter);

    }

    public static String isPhonePluggedIn(Context context) {
        boolean charging = false;
        String result = "No";
        final Intent batteryIntent = context.registerReceiver(null,
                new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        int status = batteryIntent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean batteryCharge = status == BatteryManager.BATTERY_STATUS_CHARGING;

        int chargePlug = batteryIntent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

        if (batteryCharge)
            charging = true;
        if (usbCharge)
            charging = true;
        if (acCharge)
            charging = true;

        if (charging){
            result = "Yes";

        }
        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                // NavUtils.navigateUpFromSameTask(this);
                // return true;
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}

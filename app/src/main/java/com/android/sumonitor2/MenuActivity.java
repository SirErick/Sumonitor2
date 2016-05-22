package com.android.sumonitor2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MenuActivity extends AppCompatActivity {
    public String address = null;
    public static String EXTRA_ADDRESS = "device_address";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Intent newint = getIntent();
        address = newint.getStringExtra(DeviceList.EXTRA_ADDRESS);
    }


    public void PantallaControl(View v){
        Intent p = new Intent (MenuActivity.this, ledControl.class);
        p.putExtra(EXTRA_ADDRESS, address);
        startActivity(p);
    }

    public void PantallaMonitor( View view){
        Intent q = new Intent (MenuActivity.this, MonitorActivity.class);
        q.putExtra(EXTRA_ADDRESS, address);
        startActivity(q);
    }

}

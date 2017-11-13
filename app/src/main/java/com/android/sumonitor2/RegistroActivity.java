package com.android.sumonitor2;

import android.os.Bundle;



        import java.util.Timer;
        import java.util.TimerTask;

        import android.app.Activity;
        import android.content.Intent;
        import android.content.pm.ActivityInfo;
        import android.os.Bundle;
        import android.view.Window;

public class RegistroActivity extends Activity {

    // Set the duration of the splash screen
    private static final long SPLASH_SCREEN_DELAY = 3500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set portrait orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Hide title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_registro);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                // Start the next activity
                Intent mainIntent = new Intent().setClass(
                        RegistroActivity.this, DeviceList.class);
                startActivity(mainIntent);

                // Close the activity so the user won't able to go back this
                // activity pressing Back button
                finish();
            }
        };

        // Simulate a long loading process on application startup.
        Timer timer = new Timer();
        timer.schedule(task, SPLASH_SCREEN_DELAY);
    }

}
/*
public class RegistroActivity extends  Activity {
    EditText et,et2;
    public static String EQUIPO="EQUIPO",NOMBRE="NOMBRE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        et= (EditText)findViewById(R.id.editText);
        et2= (EditText)findViewById(R.id.editText2);
    }
    public void  Menu(View v){
        String equipo=et.getText().toString();
        String nombre=et2.getText().toString();




        Intent p = new Intent(RegistroActivity.this, DeviceList.class);
        p.putExtra(EQUIPO,equipo);
        p.putExtra(NOMBRE,nombre);
        startActivity(p);
    }
}
*/
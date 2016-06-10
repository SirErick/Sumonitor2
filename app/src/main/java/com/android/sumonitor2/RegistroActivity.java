package com.android.sumonitor2;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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

package com.android.sumonitor2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class MuestraActivity extends AppCompatActivity {



    TextView tv;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muestra);
        tv = (TextView) findViewById(R.id.textView);
        leerFicheroMemoriaInterna();
    }


    private void leerFicheroMemoriaInterna()
    {
        InputStreamReader flujo=null;
        BufferedReader lector=null;
        try
        {
            flujo= new InputStreamReader(openFileInput("pruebaFichero.txt"));
            lector= new BufferedReader(flujo);
            String texto = lector.readLine();
            String newtexto= "";
            while(texto!=null)
            {


                newtexto=newtexto+texto+"\n";
                texto=lector.readLine();


            }
            tv.setText(newtexto);
            flujo.close();
            lector.close();
        }
        catch (Exception ex)
        {
            Log.e("erick", "Error al leer fichero desde memoria interna");
        }
        finally
        {
            try {
                if(flujo!=null)
                    flujo.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

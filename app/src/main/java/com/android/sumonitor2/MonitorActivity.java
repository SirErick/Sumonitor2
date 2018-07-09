package com.android.sumonitor2;



import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.UUID;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MonitorActivity extends Activity {
    Drawable drawable1 ;
    Drawable drawable2 ;
    double valorSensor=0;
    ImageView iv;
    Button btnOn, btnOff, btnTracking,btEscribir;
    TextView txtArduino, txtString, txtStringLength, sensorView0, sensorView1, sensorView2, sensorView3;
    Handler bluetoothIn;
    String sensor0,sensor1,sensor2,sensor3,equipo="",nombre="";
    final int handlerState = 0;                        //used to identify handler message
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder recDataString = new StringBuilder();

    private ConnectedThread mConnectedThread;

    // SPP UUID service - this should work for most devices
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // String for MAC address
    private static String address;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_monitor);

        drawable1 = this.getResources().getDrawable(R.drawable.negro);
        drawable2 = this.getResources().getDrawable(R.drawable.blanco);

        iv=(ImageView)findViewById(R.id.imageView);

        Intent newint = getIntent();
        Intent newint2= getIntent();
        //equipo = String.valueOf(newint.getStringExtra(MenuActivity.EQUIPO));
        //nombre= String.valueOf(newint2.getStringExtra(MenuActivity.NOMBRE));


        //Toast.makeText(getBaseContext(), equipo+" "+nombre, Toast.LENGTH_SHORT).show();
        //Link the buttons and textViews to respective views
       // btnOn = (Button) findViewById(R.id.buttonOn);
        //btnOff = (Button) findViewById(R.id.buttonOff);
       // btnTracking = (Button) findViewById(R.id.buttonTracking);
        txtString = (TextView) findViewById(R.id.txtString);
        txtStringLength = (TextView) findViewById(R.id.testView1);
        sensorView0 = (TextView) findViewById(R.id.sensorView0);
        sensorView1 = (TextView) findViewById(R.id.sensorView1);
        sensorView2 = (TextView) findViewById(R.id.sensorView2);
        sensorView3 = (TextView) findViewById(R.id.sensorView3);

        //btEscribir=(Button) findViewById(R.id.buttonEscribir);
        /*
        btEscribir.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                escribirFicheroMemoriaInterna();
            }
        });
*/

        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == handlerState) {                                     //if message is what we want
                    String readMessage = (String) msg.obj;                                                                // msg.arg1 = bytes from connect thread
                    recDataString.append(readMessage);                                      //keep appending to string until ~
                    int endOfLineIndex = recDataString.indexOf("~");                    // determine the end-of-line
                    if (endOfLineIndex > 0) {                                           // make sure there data before ~
                        String dataInPrint = recDataString.substring(0, endOfLineIndex);    // extract string
                        txtString.setText("Data Received = " + dataInPrint);
                        int dataLength = dataInPrint.length();                          //get length of data received
                        txtStringLength.setText("String Length = " + String.valueOf(dataLength));

                        if (recDataString.charAt(0) == '#')                             //if it starts with # we know it is what we are looking for
                        {
                             sensor0 = recDataString.substring(1, 5);             //get sensor value from string between indices 1-5
                            sensor1 = recDataString.substring(8, 12);
                            sensor2 = recDataString.substring(14, 18);
                            sensor3 = recDataString.substring(19, 23);
                            valorSensor=Double.valueOf(sensor0);
                            if (valorSensor>150){
                                iv.setImageDrawable(drawable1);
                                //negro

                            }
                            else{
                                iv.setImageDrawable(drawable2);

                            }
                            sensorView0.setText(" Sensor de Linea      = " + sensor0 + "");    //update the textviews with sensor values
                            sensorView1.setText(" Distancia Sensor 1 = " + sensor1 + "cm");
                            sensorView2.setText(" Distancia Sensor 2 = " + sensor2 + "cm");
                            sensorView3.setText("Estado de Funcion  = " + sensor3 + "");
                        }
                        recDataString.delete(0, recDataString.length());                    //clear all string data
                        // strIncom =" ";
                        dataInPrint = " ";
                    }
                }
            }
        };

        btAdapter = BluetoothAdapter.getDefaultAdapter();       // get Bluetooth adapter
        checkBTState();

        // Set up onClick listeners for buttons to send 1 or 0 to turn on/off LED
        /*
        btnOff.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (btSocket!=null)
                {
                    try
                    {
                        btSocket.getOutputStream().write("INICIO".toString().getBytes());
                    }
                    catch (IOException e)
                    {
                        msg("Error");
                    }
                }
                Toast.makeText(getBaseContext(), "PELEA INICIADA", Toast.LENGTH_SHORT).show();
            }
        });

        btnOn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (btSocket!=null)
                {
                    try
                    {
                        btSocket.getOutputStream().write("FIN".toString().getBytes());
                    }
                    catch (IOException e)
                    {
                        msg("Error");
                    }
                }
                Toast.makeText(getBaseContext(), "PELEA TERMINADA", Toast.LENGTH_SHORT).show();

            }
        });
        btnTracking.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (btSocket!=null)
                {
                    try
                    {
                        btSocket.getOutputStream().write("TRACKING".toString().getBytes());
                    }
                    catch (IOException e)
                    {
                        msg("Error");
                    }
                }
                Toast.makeText(getBaseContext(), "TRACKING ACTIVADO", Toast.LENGTH_SHORT).show();

            }
        });*/
    }


    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {

        return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
        //creates secure outgoing connecetion with BT device using UUID
    }

    /*
    private void escribirFicheroMemoriaInterna()
    {
        Toast.makeText(getBaseContext(), "Registrado", Toast.LENGTH_SHORT).show();
        OutputStreamWriter escritor=null;
        try
        {
            escritor=new OutputStreamWriter(openFileOutput("pruebaFichero.txt", Context.MODE_APPEND));

            escritor.write(sensor0+";"+sensor1+";"+sensor2+";"+sensor3+"\n");


        }
        catch (Exception ex)
        {
            Log.e("erick", "Error al escribir fichero a memoria interna");
        }
        finally
        {
            try {
                if(escritor!=null)
                    escritor.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
*/
    @Override
    public void onResume() {
        super.onResume();

        //Get MAC address from DeviceListActivity via intent
        Intent intent = getIntent();

        //Get the MAC address from the DeviceListActivty via EXTRA
        address = intent.getStringExtra(MenuActivity.EXTRA_ADDRESS);

        //create device and set the MAC address
        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_LONG).show();
        }
        // Establish the Bluetooth socket connection.
        try
        {
            btSocket.connect();
        } catch (IOException e) {
            try
            {
                btSocket.close();
            } catch (IOException e2)
            {
                //insert code to deal with this
            }
        }
        mConnectedThread = new ConnectedThread(btSocket);
        mConnectedThread.start();

        //I send a character when resuming.beginning transmission to check device is connected
        //If it is not an exception will be thrown in the write method and finish() will be called
        mConnectedThread.write("x");
    }
    private void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }
    @Override
    public void onPause()
    {
        super.onPause();
        try
        {
            //Don't leave Bluetooth sockets open when leaving activity
            btSocket.close();
        } catch (IOException e2) {
            //insert code to deal with this
        }
    }

    //Checks that the Android device Bluetooth is available and prompts to be turned on if off
    private void checkBTState() {

        if(btAdapter==null) {
            Toast.makeText(getBaseContext(), "Device does not support bluetooth", Toast.LENGTH_LONG).show();
        } else {
            if (btAdapter.isEnabled()) {
            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }

    //create new class for connect thread
    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        //creation of the connect thread
        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                //Create I/O streams for connection
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[256];
            int bytes;

            // Keep looping to listen for received messages
            while (true) {
                try {
                    bytes = mmInStream.read(buffer);            //read bytes from input buffer
                    String readMessage = new String(buffer, 0, bytes);
                    // Send the obtained bytes to the UI Activity via handler
                    bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }
        //write method
        public void write(String input) {
            byte[] msgBuffer = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(msgBuffer);                //write bytes over BT connection via outstream
            } catch (IOException e) {
                //if you cannot write, close the application
                Toast.makeText(getBaseContext(), "Conexion fallida", Toast.LENGTH_LONG).show();
                finish();

            }
        }
    }
}
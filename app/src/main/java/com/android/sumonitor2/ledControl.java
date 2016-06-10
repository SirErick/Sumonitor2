package com.android.sumonitor2;


    import android.support.v7.app.ActionBarActivity;
    import android.os.Bundle;
    import android.view.Menu;
    import android.view.MenuItem;

    import android.bluetooth.BluetoothSocket;
    import android.content.Intent;
    import android.view.View;
    import android.widget.Button;
    import android.widget.SeekBar;
    import android.widget.TextView;
    import android.widget.Toast;
    import android.app.ProgressDialog;
    import android.bluetooth.BluetoothAdapter;
    import android.bluetooth.BluetoothDevice;
    import android.os.AsyncTask;

    import java.io.IOException;
    import java.util.UUID;


    public class ledControl extends ActionBarActivity {

        Button btnOn, btnOff, btnDis, btnizq, btnder;
        SeekBar brightness;
        TextView lumn;
        String address = null;
        private ProgressDialog progress;
        BluetoothAdapter myBluetooth = null;
        BluetoothSocket btSocket = null;
        private boolean isBtConnected = false;
        //SPP UUID. Look for it
        static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            Intent newint = getIntent();
            address = newint.getStringExtra(MenuActivity.EXTRA_ADDRESS); //receive the address of the bluetooth device

            //vista de nuevo activity
            setContentView(R.layout.activity_led_control);

            //botones
            btnOn = (Button) findViewById(R.id.button2);
            btnOff = (Button) findViewById(R.id.button3);
            btnDis = (Button) findViewById(R.id.button4);
            btnizq= (Button) findViewById(R.id.button5);
            btnder=(Button) findViewById(R.id.button6);
//..............

            new ConnectBT().execute(); //Call the class to connect

            //commands to be sent to bluetooth
            btnOn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    turnOnLed();      //method to turn on
                }
            });
            btnOff.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    turnOffLed();   //method to turn off
                }
            });
            btnDis.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Disconnect(); //close connection
                }
            });
            btnizq.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    izquierda();      //method to turn on
                }
            });
            btnder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    derecha();      //method to turn on
                }
            });
        }

//yes



        //yes
        private void Disconnect()
        {
            if (btSocket!=null) //If the btSocket is busy
            {
                try
                {
                    btSocket.close(); //close connection
                }
                catch (IOException e)
                { msg("Error");}
            }
            finish(); //return to the first layout

        }
        //yes
        private void turnOffLed()
        {
            if (btSocket!=null)
            {
                try
                {
                    btSocket.getOutputStream().write("TF".toString().getBytes());
                }
                catch (IOException e)
                {
                    msg("Error");
                }
            }
        }
        //yes
        private void turnOnLed()
        {
            if (btSocket!=null)
            {
                try
                {
                    btSocket.getOutputStream().write("TO".toString().getBytes());
                }
                catch (IOException e)
                {
                    msg("Error");
                }
            }
        }
        private void derecha()
        {
            if (btSocket!=null)
            {
                try
                {
                    btSocket.getOutputStream().write("DER".toString().getBytes());
                }
                catch (IOException e)
                {
                    msg("Error");
                }
            }
        }
        private void izquierda()
        {
            if (btSocket!=null)
            {
                try
                {
                    btSocket.getOutputStream().write("IZQ".toString().getBytes());
                }
                catch (IOException e)
                {
                    msg("Error");
                }
            }
        }

        // YES... fast way to call Toast
        private void msg(String s)
        {
            Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
        }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_led_control, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);

    }
*/

        //yes
        private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
        {
            private boolean ConnectSuccess = true; //para ver si ests conectado

            @Override
            protected void onPreExecute()
            {
                progress = ProgressDialog.show(ledControl.this, "Conectando...", "Espere!!!");  //mostrar un dialogo de proceso
            }

            @Override
            protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
            {
                try
                {
                    if (btSocket == null || !isBtConnected)
                    {
                        myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                        BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                        btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                        BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                        btSocket.connect();//start connection
                    }
                }
                catch (IOException e)
                {
                    ConnectSuccess = false;//if the try failed, you can check the exception here
                }
                return null;
            }
            @Override
            protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
            {
                super.onPostExecute(result);

                if (!ConnectSuccess)
                {
                    msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                    finish();
                }
                else
                {
                    msg("Connected.");
                    isBtConnected = true;
                }
                progress.dismiss();
            }
        }
}
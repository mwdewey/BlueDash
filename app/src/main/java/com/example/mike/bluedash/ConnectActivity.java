package com.example.mike.bluedash;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Set;


public class ConnectActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        initBluetooth();
    }


   private void initBluetooth(){
       BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
       if (mBluetoothAdapter == null) {
           TextView bottomTextView = (TextView) findViewById(R.id.bottomTextView);
           bottomTextView.setText("Device does not support bluetooth.");
           return;
       }

       if (!mBluetoothAdapter.isEnabled()) {
           Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
           startActivityForResult(enableBtIntent, 0);
       }

       Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
       if (pairedDevices.size() > 0) {
           for (BluetoothDevice device : pairedDevices) {
               // Add the name and address to an array adapter to show in a ListView
               mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
           }
       }



   }

}

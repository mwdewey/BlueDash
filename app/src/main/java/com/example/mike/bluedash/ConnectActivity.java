package com.example.mike.bluedash;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class ConnectActivity extends ActionBarActivity{

    BroadcastReceiver mReceiver;
    List<String> bList;
    List<BluetoothDevice> bDeviceList;
    ListView bluetoothList;
    BluetoothAdapter mBluetoothAdapter;

    @Override
     public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        mReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                // When discovery finds a device
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    // Get the BluetoothDevice object from the Intent
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    // Add the name and address to an array adapter to show in a ListView
                    //bList.add(device.getName() + "\n" + device.getAddress());
                    bList.add(device.getName() + ":" + device.getAddress());
                    bDeviceList.add(device);
                    Log.d("debug",device.getName() + ":" + device.getAddress());
                    ((ArrayAdapter)bluetoothList.getAdapter()).notifyDataSetChanged();
                }
            }
        };

        bluetoothList = (ListView) findViewById(R.id.listView);
        bluetoothList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BluetoothDevice b = bDeviceList.get(position);
                Log.d("debug","Picked: " + b.getName());

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("address",b.getAddress());
                startActivity(i);

            }
        });



        bList = new ArrayList<>();
        bDeviceList = new ArrayList<>();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        initBluetooth();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
        mBluetoothAdapter.cancelDiscovery();
    }


   private void initBluetooth(){

       if (mBluetoothAdapter == null) {
           Toast.makeText(getApplicationContext(),"Device does not support bluetooth.", Toast.LENGTH_LONG).show();
           return;
       }

       if (!mBluetoothAdapter.isEnabled()) {
           Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
           startActivityForResult(enableBtIntent, 0);
       }

       Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
       for(BluetoothDevice b : pairedDevices){
           bList.add(b.getName());
           bDeviceList.add(b);
       }

       ArrayAdapter blueArray = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, bList);
       bluetoothList.setAdapter(blueArray);

       IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
       registerReceiver(mReceiver, filter);

       mBluetoothAdapter.startDiscovery();



   }

}

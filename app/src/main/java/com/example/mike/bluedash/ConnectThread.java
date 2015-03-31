package com.example.mike.bluedash;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Message;
import android.provider.ContactsContract;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by Mike on 3/28/2015.
 */
public class ConnectThread extends Thread {
    private BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;
    private static final UUID MY_UUID = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    public ConnectThread(BluetoothDevice device) {
        // Use a temporary object that is later assigned to mmSocket,
        // because mmSocket is final
        BluetoothSocket tmp = null;
        mmDevice = device;

        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try {
            // MY_UUID is the app's UUID string, also used by the server code
            tmp = (BluetoothSocket) device.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(device,1);
        } catch (Exception e) { Log.d("debug","Error creating socket."); }

        mmSocket = tmp;
    }

    public void run() {

        boolean connected = false;
        int attempts = 1;
        while(!connected && attempts < 2) {
            try {
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                mmSocket = (BluetoothSocket) mmDevice.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(mmDevice,attempts);
                mmSocket.connect();
                Log.d("debug", "Socket connected, port:" + attempts);
                connected = true;
            } catch (Exception e) {
                // Unable to connect; close the socket and get out
                Log.d("debug", "Socket failed to connect, port:" + attempts);
                e.printStackTrace();
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                    Log.d("debug", "Socked error failed to close, port:" + attempts);
                }
            }
            attempts++;
        }

        // Do work to manage the connection (in a separate thread)
        //BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
        //ConnectedThread connectedThread = new ConnectedThread(mmSocket);
        //connectedThread.start();
        InputStream inStream = null;
        try { inStream = mmSocket.getInputStream(); } catch (IOException e) { }

        while (true) {
            try {
                // Read from the InputStream
                StringBuilder sb = new StringBuilder();
                int i;
                while (0 <= (i = inStream.read())) {
                    if (i == '\n') {
                        break;
                    } else sb.append((char)i);
                }

                // Send the obtained bytes to the UI activity
                BluetoothDataHolder.getInstance().addData(sb.toString());


                Log.d("debug",String.valueOf(sb.toString()));
            } catch (IOException e) {
                break;
            }
        }


    }




    /** Will cancel an in-progress connection, and close the socket */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }
}
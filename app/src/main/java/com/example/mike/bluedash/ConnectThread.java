package com.example.mike.bluedash;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by Mike on 3/28/2015.
 */
public class ConnectThread extends Thread {
    private final BluetoothSocket mmSocket;
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

        try {
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
            Log.d("debug","Socket name: " + mmSocket.getRemoteDevice().getName());
            mmSocket.connect();
            Log.d("debug","Socked connected");
        } catch (IOException connectException) {
            // Unable to connect; close the socket and get out
            try {
                mmSocket.close();
                Log.d("debug",connectException.getMessage());
                connectException.printStackTrace();
            } catch (IOException closeException) { Log.d("debug","Socked error"); }
            return;
        }

        // Do work to manage the connection (in a separate thread)
        //BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
        //ConnectedThread connectedThread = new ConnectedThread(mmSocket);
        //connectedThread.start();
        OutputStream outStream = null;
        try { outStream = mmSocket.getOutputStream(); } catch (IOException e) { }

        String message = "Hello from Android.\n";
        byte[] msgBuffer = message.getBytes();
        try { outStream.write(msgBuffer); } catch (IOException e) { }

    }

    /** Will cancel an in-progress connection, and close the socket */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }
}
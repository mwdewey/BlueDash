package com.example.mike.bluedash;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AsyncUpdate extends AsyncTask<String, Void, String> {

    private ActionBarActivity main;
    private String address;
    private boolean isConnected;
    private BluetoothSocket mmSocket;
    private BluetoothDevice mmDevice;
    private InputStream inStream;

    private List<String> bluetoothStack;
    private String bluetoothLine;

    private int packetNum;

    private CircleComponent dial;

    public AsyncUpdate(ActionBarActivity main, String address){
        super();

        this.main = main;
        this.address = address;

        isConnected = false;
        mmDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address);
        inStream = null;
        bluetoothStack = new ArrayList<>();
        bluetoothLine = "";
        packetNum = -1;

        dial = (CircleComponent) this.main.findViewById(8000);
    }

    @Override
    protected String doInBackground(String... params) {
        if(!isConnected) {
            if(!connect()) return "Failed to connect";
        }

        while (true) {
            try {
                StringBuilder sb = new StringBuilder();
                int i;
                while (0 <= (i = inStream.read())) {
                    if (i == '\n') {
                        break;
                    } else sb.append((char)i);
                }

                bluetoothLine = sb.toString();

                packetNum = parse(bluetoothLine);

                publishProgress();

            } catch (IOException e) {
                break;
            }
        }

        return "Executed";
    }

    @Override
    protected void onPostExecute(String result) {
        // might want to change "executed" for the returned string passed
        // into onPostExecute() but that is upto you
    }

    @Override
    protected void onPreExecute() {}

    @Override
    protected void onProgressUpdate(Void... values) {
        CircleComponent dial = (CircleComponent) main.findViewById(8000);
        if (dial != null) {
            Log.d("onProgressUpdate","Number: " + getNum());
            dial.updateLine(getNum());
            dial.invalidate();
        }

    }

    public int getNum(){return packetNum;}
    public CircleComponent getDial(){return dial;}

    private boolean connect(){
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

                return false;
            }
            attempts++;
        }

        try { inStream = mmSocket.getInputStream();
        } catch (IOException e) {
            Log.d("debug", "Failed to open input stream");
            return false;
        }

        isConnected = true;
        return true;
    }

    private int parse(String raw){
        if(raw.equals("")) return -1;

        JSONObject jObject = null;
        try {
            jObject = new JSONObject(raw);
        }
        catch (JSONException e){ Log.d("error","Error parsing JSON from server"); return -1; }

        int packetNum;
        try {
            packetNum = jObject.getInt("num");
        }
        catch (JSONException e){ Log.d("error","Error no packet number found"); return -1; }

        return packetNum;
    }


}
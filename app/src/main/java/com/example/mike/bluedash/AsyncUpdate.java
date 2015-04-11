package com.example.mike.bluedash;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AsyncUpdate extends AsyncTask<String, Void, String> {

    final String CIRCLE_COMPONENT_NAME = "CircleComponent";
    final String BAR_COMPONENT_NAME = "GuiComponent";

    private int id;
    private ActionBarActivity main;
    private String address;
    private boolean isConnected;
    private BluetoothSocket mmSocket;
    private BluetoothDevice mmDevice;
    private InputStream inStream;

    private List<String> bluetoothStack;
    private String bluetoothLine;

    private int packetNum;

    View component;
    HashMap viewCache;
    List<HashMap<Integer,Integer>> queue;

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

        viewCache = new HashMap<Integer, Integer>();
        queue = new ArrayList<>();
    }

    public View getComponent(){
        return this.component;
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

                while(!queue.isEmpty()) {
                    publishProgress();
                }

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

    public HashMap<Integer, View> getViewCache() {
        return viewCache;
    }

    public List<HashMap<Integer,Integer>> getQueue(){return this.queue;}

    @Override
    protected void onProgressUpdate(Void... values) {

        if(getQueue().isEmpty()) return;
        HashMap map = getQueue().get(0);
        getQueue().remove(0);
        int key = (Integer) map.keySet().toArray()[0];
        int id = key;
        int value = (Integer) map.get(key);

        if (viewCache.containsKey(id)) {
            component = (View)viewCache.get(id);
        } else {
            getViewCache().put(id, main.findViewById(id));
            component = main.findViewById(id);
        }
        View tempComponent = null;

        switch(component.getClass().getSimpleName()){
            case CIRCLE_COMPONENT_NAME:
                tempComponent = component;
                ((CircleComponent)tempComponent).updateLine(value);
                tempComponent.invalidate();
                break;
            case BAR_COMPONENT_NAME:
                tempComponent = component;
                ((GuiComponent)tempComponent).progress = value;
                tempComponent.invalidate();
                break;
            default: break;
        }

//        if (component != null) {
//            Log.d("onProgressUpdate","Number: " + getNum());
//        }

    }

    public int getNum(){return packetNum;}

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
            id = jObject.getInt("id");
            packetNum = jObject.getInt("num");

            HashMap<Integer,Integer> temp = new HashMap<>();
            temp.put(id,packetNum);
            queue.add(temp);
        }
        catch (JSONException e){ Log.d("error","Error no packet number found"); return -1; }

        return packetNum;
    }


}
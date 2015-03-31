package com.example.mike.bluedash;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mike on 3/30/2015.
 */
public class BluetoothDataHolder {
    private List<String> data = new ArrayList<>();
    public String getData() {
        if(data.isEmpty()) return "";
        else return data.remove(0);

    }
    public void addData(String d) {this.data.add(d);}

    private static final BluetoothDataHolder holder = new BluetoothDataHolder();
    public static BluetoothDataHolder getInstance() {return holder;}
}

package com.example.bluetooth;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
ListView listView;
Button button;
TextView textView;
ArrayList<String> bluetoothdevices;
ArrayList<String> addresses;
ArrayAdapter<String> arrayAdapter;
BluetoothAdapter bluetoothAdapter;
private  final BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action=intent.getAction();
        if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
            textView.setText("Finished");
            button.setEnabled(true);
            Log.e("device","found");
        }
        else if(BluetoothDevice.ACTION_FOUND.equals(action))
        {

            BluetoothDevice bluetoothDevice=intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            String name=bluetoothDevice.getName();
            String address=bluetoothDevice.getAddress();
            String rssi= Integer.toString(intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,Short.MIN_VALUE));

            if(!addresses.contains(address))
            {
                addresses.add(address);
                String deviceString="";
                if(name.equals(null) || name.equals(""))
                {
                    deviceString="name:"+"  address:"+address+" -RSSI"+rssi+"dBm";
                }
                else
                {
                    deviceString="name:"+name+"  address:"+address+" -RSSI"+rssi+"dBm";
                }
                if(!bluetoothdevices.contains(deviceString))
                {
                    bluetoothdevices.add(deviceString);
                }
                arrayAdapter.notifyDataSetChanged();
            }

            //  Log.i("Device details","Name"+name+"Address"+address+"rssi"+rssi);
        }
    }
};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView=findViewById(R.id.list);
        textView=findViewById(R.id.textView);
        button=findViewById(R.id.button);
        bluetoothdevices=new ArrayList<>();
        addresses=new ArrayList<>();
        arrayAdapter=new ArrayAdapter(this, android.R.layout.simple_list_item_1,bluetoothdevices);
        bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        listView.setAdapter(arrayAdapter);

        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(broadcastReceiver,intentFilter);

        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("Searching");
                bluetoothdevices.clear();
                button.setEnabled(false);
                addresses.clear();
                bluetoothAdapter.startDiscovery();
            }
        });
      //  BluetoothSocket bluetoothSocket=new BluetoothSocket();
    }
}
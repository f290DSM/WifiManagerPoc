package dev.sdras.wifimanagerpoc;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

public class WifiReceiver extends BroadcastReceiver {

    WifiManager wifiManager;
    StringBuilder stringBuilder;
    ListView listViewWifiDeviceList;

    public WifiReceiver(WifiManager wifiManager, ListView listViewWifiDeviceList) {
        this.wifiManager = wifiManager;
        this.listViewWifiDeviceList = listViewWifiDeviceList;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(action)) {
            stringBuilder = new StringBuilder();

            List<ScanResult> wifiList = wifiManager.getScanResults();
            ArrayList<String> deviceList = new ArrayList<>();

            for (ScanResult scanResult: wifiList) {
                stringBuilder.append("").append(scanResult.SSID).append(" - ").append(scanResult.capabilities);
                        deviceList.add(scanResult.SSID + " - " + scanResult.capabilities   );
            }
            Toast.makeText(context, stringBuilder.toString(), Toast.LENGTH_SHORT).show();
            ArrayAdapter arrayAdapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, deviceList.toArray());
            listViewWifiDeviceList.setAdapter(arrayAdapter);
        }
    }
}

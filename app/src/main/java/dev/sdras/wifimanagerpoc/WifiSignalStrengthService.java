package dev.sdras.wifimanagerpoc;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import java.util.List;

public class WifiSignalStrengthService extends Service {

    private WifiManager wifiManager;
    private BroadcastReceiver wifiScanReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifiScanReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    List<ScanResult> scanResults = wifiManager.getScanResults();
                    if (scanResults != null && !((List<?>) scanResults).isEmpty()) {
                        // Encontrar a rede com o sinal mais forte
                        ScanResult strongestSignal = findStrongestSignal(scanResults);
                        if (strongestSignal != null) {
                            int signalStrength = strongestSignal.level;
                            // TODO: Enviar notificação para filtrar os registros Firestore com base na rede mais forte.
                            Log.d("SignalStrength", "Strongest Signal Strength: " + signalStrength + " dBm");
                        }
                    }
                }
                Toast.makeText(getApplicationContext(), "Sem permissao...", Toast.LENGTH_SHORT).show();
            }
        };
        registerReceiver(wifiScanReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Inicie a lógica de monitoramento das redes Wi-Fi aqui
        startWifiScan();
        return START_STICKY;
    }

    private void startWifiScan() {
        wifiManager.startScan();
    }

    private ScanResult findStrongestSignal(List<ScanResult> scanResults) {
        ScanResult strongestSignal = null;
        int maxSignalStrength = Integer.MIN_VALUE;

        for (ScanResult result : scanResults) {
            if (result.level > maxSignalStrength) {
                strongestSignal = result;
                maxSignalStrength = result.level;
            }
        }

        return strongestSignal;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(wifiScanReceiver);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}


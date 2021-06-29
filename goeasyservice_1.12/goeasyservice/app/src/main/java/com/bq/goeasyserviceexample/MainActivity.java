package com.bq.goeasyserviceexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bq.goeasygnssdatalib.UbloxDeviceConnectionListener;
import com.bq.goeasygnssdatalib.gnssdata.GnssData;
import com.bq.goeasygnssdatalib.GnssDataListener;
import com.bq.goeasygnssdatalib.GoeasyService;
import com.bq.goeasygnssdatalib.ubxdata.ParserUtils;

public class MainActivity extends AppCompatActivity implements GnssDataListener {


    private final static String TAG = "GOEASY_MainActivity";

    private Activity myActivity;

    TextView txtNavPosllh, txtNavSat, txtNavClock, txtNavTimegal, txtRxmSfrbx, txtGnssData;

    private GoeasyService mBoundService;

    private boolean mIsBound;

    UbloxDeviceConnectionListener deviceConnectionListener = new UbloxDeviceConnectionListener() {
        @Override
        public void onDeviceConnectedCallback(boolean connected) {
            Toast.makeText(myActivity, connected ? "Ublox device connected" : "Ublox device disconnected", Toast.LENGTH_SHORT).show();
        }
    };

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mBoundService = ((GoeasyService.LocalBinder) service).getService();
            mBoundService.registerDataListener((MainActivity) myActivity);
            mBoundService.registerDeviceConnectionListener(deviceConnectionListener);
        }

        public void onServiceDisconnected(ComponentName className) {
            mBoundService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myActivity = this;

        isStoragePermissionGranted();
        
        txtNavPosllh = findViewById(R.id.NavPosllh);
        txtNavSat = findViewById(R.id.NavSat);
        txtNavTimegal = findViewById(R.id.NavTimegal);
        txtNavClock = findViewById(R.id.NavClock);
        txtRxmSfrbx = findViewById(R.id.RxmSfrbx);
        txtGnssData = findViewById(R.id.GnssData);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doBindService();
            }
        });

    }

    void doBindService() {
        bindService(new Intent(MainActivity.this,
                GoeasyService.class), mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService() {
        if (mIsBound) {
            if (mBoundService != null) {
                mBoundService.unregisterDataListener(this);
            }
            unbindService(mConnection);
            mIsBound = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnbindService();
    }

    @Override
    public void onDataChangedCallback(GnssData data) {
        showResults(data);
        ParserUtils.writeStringToFile(data.getUbxDataGroup().message, "GoeasyRawLog.txt");
        ParserUtils.writeToFile(data.getUbxDataGroup().message, data.getUbxDataGroup().toString(), "GoeasyParsedLog.txt");
        ParserUtils.writeStringToFile(data.toString() + "\n\n\n\n\n\n", "GoeasyParsedLog.txt");
    }

    public void showResults(final GnssData data) {
        runOnUiThread(new Runnable() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                txtGnssData.setText(data.toString());

                txtNavPosllh.setText(data.getUbxDataGroup().navPosllhRaw + "\n\n" + data.getUbxDataGroup().navPosllh);
                txtNavSat.setText(data.getUbxDataGroup().navSatRaw + "\n\n" + data.getUbxDataGroup().navSat);
                txtNavClock.setText(data.getUbxDataGroup().navClockRaw + "\n\n" + data.getUbxDataGroup().navClock);
                txtNavTimegal.setText(data.getUbxDataGroup().navTimegalRaw + "\n\n" + data.getUbxDataGroup().navTimegal);
                txtRxmSfrbx.setText(data.getUbxDataGroup().rxmSfrbxRaw + "\n\n" + data.getUbxDataGroup().rxmSfrbx);

            }
        });
    }

    public  boolean isStoragePermissionGranted() {
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG,"Permission is granted");
            return true;
        } else {
            Log.v(TAG,"Permission is revoked");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            return false;
        }

    }

}

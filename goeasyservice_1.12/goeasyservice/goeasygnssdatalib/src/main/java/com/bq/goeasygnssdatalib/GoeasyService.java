package com.bq.goeasygnssdatalib;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.bq.goeasygnssdatalib.ubxdata.UbxDataGroup;
import com.bq.goeasygnssdatalib.ubxdata.UbxDataParser;
import com.bq.goeasygnssdatalib.gnssdata.GnssData;
import com.physicaloid.lib.Physicaloid;
import com.physicaloid.lib.usb.driver.uart.ReadLisener;

import java.util.ArrayList;

public class GoeasyService extends Service {

    private final static String TAG = "GOEASY_SERVICE";

    private Physicaloid mPhysicaloid;
    private BroadcastReceiver detachReceiver;
    private UbxDataGroup ubxDataGroup;


    private final ArrayList<GnssDataListener> mDataListeners = new ArrayList<>();
    private final ArrayList<UbloxDeviceConnectionListener> mUbloxDeviceConnectionListener = new ArrayList<>();

    public GoeasyService() {
    }

    public void registerDataListener(GnssDataListener listener) {
        mDataListeners.add(listener);
    }

    public void unregisterDataListener(GnssDataListener listener) {
        mDataListeners.remove(listener);
    }

    public void registerDeviceConnectionListener(UbloxDeviceConnectionListener listener) {
        mUbloxDeviceConnectionListener.add(listener);
    }

    public void unregisterDeviceConnectionListener(GnssDataListener listener) {
        mUbloxDeviceConnectionListener.remove(listener);
    }

    private void sendDataUpdate(GnssData ubxDataGroup) {
        for (int i = mDataListeners.size() - 1; i >= 0; i--) {
            mDataListeners.get(i).onDataChangedCallback(ubxDataGroup);
        }
    }

    private void sendDeviceConnectionUpdate(boolean connected) {
        for (int i = mUbloxDeviceConnectionListener.size() - 1; i >= 0; i--) {
            mUbloxDeviceConnectionListener.get(i).onDeviceConnectedCallback(connected);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v("GOEASY", "Goeasy service started");

        sendDeviceConnectionUpdate(true);

        mPhysicaloid = new Physicaloid(this);

        onOpen();

        ubxDataGroup = new UbxDataGroup();

        detachReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(UsbManager.ACTION_USB_DEVICE_DETACHED)) {
                    onClose();
                    Log.v(TAG, "Goeasy Usb device dettached");
                    sendDeviceConnectionUpdate(false);
                    unregisterReceiver(detachReceiver);
                    stopSelf();
                }
            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        registerReceiver(detachReceiver, filter);
        return START_STICKY;
    }

    public class LocalBinder extends Binder {
        public GoeasyService getService() {
            return GoeasyService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private final IBinder mBinder = new LocalBinder();

    public void onOpen() {
        mPhysicaloid.setBaudrate(9600);
        if (mPhysicaloid.open()) {
            mPhysicaloid.addReadListener(new ReadLisener() {
                @Override
                public void onRead(int size) {
                    byte[] buf = new byte[size];
                    mPhysicaloid.read(buf, size);
                    String message = new String(buf);
                    try {
                        ubxDataGroup = parseMessage(message, ubxDataGroup);
                    } catch (Exception e) {
                        Log.e(TAG, "Exception when parsing the message " + e);
                        Log.e(TAG, "RawMessage: " + message);
                    }
                    GnssData gnssData = new GnssData(ubxDataGroup);
                    sendDataUpdate(gnssData);

                }
            });
        } else {
            Log.i(TAG, "Cannot connect to usb device");
        }
    }

    UbxDataGroup parseMessage(String message, UbxDataGroup ubxDataGroup) {
        try {
            ubxDataGroup = UbxDataParser.parseGnssDataString(message, ubxDataGroup);
        } catch (NumberFormatException e) {
            Log.e(TAG, "There was a problem with some data ( " + message + " )" , e);
        }
        if (ubxDataGroup == null)
            ubxDataGroup = new UbxDataGroup();
        return ubxDataGroup;
    }

    public void onClose() {
        if (mPhysicaloid.close()) {
            mPhysicaloid.clearReadListener();
        }
    }

}

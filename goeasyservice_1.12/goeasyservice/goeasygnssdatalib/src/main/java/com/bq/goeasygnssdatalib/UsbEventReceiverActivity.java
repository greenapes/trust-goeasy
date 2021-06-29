package com.bq.goeasygnssdatalib;

import android.app.Activity;
import android.content.Intent;
import android.hardware.usb.UsbManager;
import android.util.Log;


/*
 * Activity that starts the data receiving service when the usb device is attached
 */
public class UsbEventReceiverActivity extends Activity {

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getAction().equals(UsbManager.ACTION_USB_DEVICE_ATTACHED)) {
                Log.v("GOEASY", "Usb device detected");
                Intent serviceIntent = new Intent(UsbEventReceiverActivity.this, GoeasyService.class);
                startService(serviceIntent);
            }
        }
        finish();
    }

}

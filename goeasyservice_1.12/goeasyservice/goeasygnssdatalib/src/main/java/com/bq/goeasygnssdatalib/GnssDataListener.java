package com.bq.goeasygnssdatalib;

import com.bq.goeasygnssdatalib.gnssdata.GnssData;

public interface GnssDataListener {
    public void onDataChangedCallback(GnssData data);
}

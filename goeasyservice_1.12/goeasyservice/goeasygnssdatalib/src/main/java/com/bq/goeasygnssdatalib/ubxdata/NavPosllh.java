package com.bq.goeasygnssdatalib.ubxdata;

import androidx.annotation.NonNull;


/*
 * Geodetic Position Solution
 * This message outputs the Geodetic position.
 */
public class NavPosllh extends UbxData {

    // Sample message

    // header(2) class+id(2)    length(2) iTOW(4)        lon(4)         lat(4)         height(4)      hMSL(4)        hAcc(4)        vAcc(4)        CHS(2)
    // B5 62     01 02          1C 00     88 32 D3 17    62 A8 AE FD    2A E1 1E 18    50 7B 0C 00    2A B7 0B 00    B7 CC 31 00    A9 5E 2F 00    66 D3

    NavPosllh() {
        messageClass = UBX_NAV_POSLLH;
    }

    // GPS time of week of the navigation epoch
    public long iTOW;

    // Longitude
    public long lon;

    // Latitude
    public long lat;

    // Height above ellipsoid
    public long height;

    // Height above mean sea level
    public long hMSL;

    // Horizontal accuracy estimate
    public long hAcc;

    // Vertical accuracy estimate
    public long vAcc;

    @NonNull
    @Override
    public String toString() {
        return "NavPosllh iTOW = " + iTOW + " lon = " + lon + " lat = " + lat + " height = " + height +
                " hMSL = " + hMSL + " hAcc = " + hAcc + " vAcc = " + vAcc;
    }

    public long getLon() {
        return lon;
    }

    public long getLat() {
        return lat;
    }

}

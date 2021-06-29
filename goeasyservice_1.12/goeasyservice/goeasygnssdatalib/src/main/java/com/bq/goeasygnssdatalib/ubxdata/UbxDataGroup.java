package com.bq.goeasygnssdatalib.ubxdata;


import androidx.annotation.NonNull;

import java.util.ArrayList;

public class UbxDataGroup {

    public String message;
    public NavPosllh navPosllh;
    public String navPosllhRaw;
    public NavSat navSat;
    public String navSatRaw;
    public NavClock navClock;
    public String navClockRaw;
    public NavTimegal navTimegal;
    public String navTimegalRaw;
    public ArrayList<RxmSfrbx> rxmSfrbx = new ArrayList<>();
    public ArrayList<String> rxmSfrbxRaw = new ArrayList<>();


    @NonNull
    @Override
    public String toString() {
        return navPosllhRaw + "\n\n" + navPosllh + "\n\n" + navSatRaw + "\n\n" + navSat +
                "\n\n" + navClockRaw + "\n\n" + navClock + "\n\n" + navTimegalRaw + "\n\n" +
                navTimegal + "\n\n" + rxmSfrbxRaw + "\n\n" + rxmSfrbx;
    }
}

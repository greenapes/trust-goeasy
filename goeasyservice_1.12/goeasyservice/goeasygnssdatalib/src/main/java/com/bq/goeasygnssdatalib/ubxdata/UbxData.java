package com.bq.goeasygnssdatalib.ubxdata;


import androidx.annotation.NonNull;

/*
 * Base class for the different ublox data classes
 */
public abstract class UbxData {


    public static final String UBX_HEADER = "B562";


    /*
    Broadcast Navigation Data Subframe
    This message reports a complete subframe of broadcast navigation data
    decoded from a single signal.
    */
    public static final String UBX_RXM_SFRBX = "0213";
    public static final String UBX_RXM_SFRBX_GPS_LENGTH = "0030";
    public static final String UBX_RXM_SFRBX_GALILEO_LENGTH = "002C";

    /*
    Geodetic Position Solution
    This message outputs the Geodetic position.
    */
    public static final String UBX_NAV_POSLLH = "0102";

    /*
    Galileo Time Solution
    This message reports the precise Galileo time of the most recent navigation
    solution including validity flags and an accuracy estimate.
    */
    public static final String UBX_NAV_TIMEGAL = "0125";

    /*
    Satellite Information
    This message displays information about SVs which are either known to be
    visible or currently tracked by the receiver.
    */
    public static final String UBX_NAV_SAT = "0135";

    /*
    Clock Solution
    */
    public static final String UBX_NAV_CLOCK = "0122";

    public String messageClass = "";

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}

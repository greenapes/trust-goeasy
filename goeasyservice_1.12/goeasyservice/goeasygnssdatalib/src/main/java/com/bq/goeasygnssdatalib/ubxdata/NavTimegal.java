package com.bq.goeasygnssdatalib.ubxdata;

import androidx.annotation.NonNull;


/*
 * Galileo Time Solution
 * This message reports the precise Galileo time of the most recent navigation
 * solution including validity flags and an accuracy estimate.
 */
public class NavTimegal extends UbxData {


    // Sample message

    // header(2) class+id(2)    length(2) iTOW(4)        galTow(4)      fGalTow(4)     galWno(2)  leapS (1)  valid(1)  tAcc(4)        CHS(2)
    // B5 62     01 25          14 00     A0 E7 C9 17    04 17 06 00    CE 18 00 00    2B 04      11         03        10 3A 31 01    67 48


    static final long E9 = 1000000000;

    NavTimegal() {
        messageClass = UBX_NAV_TIMEGAL;
    }


    // GPS time of week of the navigation epoch
    public long iTOW;

    // Galileo time of week (rounded to seconds)
    public long galTow;

    // Fractional part of the Galileo time of week (range: +/-500000000).
    // The precise Galileo time of week in seconds is: galTow + fGalTow * 1e-9
    public long fGalTow;

    // Galileo week number
    public long galWno;

    // Galileo leap seconds (Galileo-UTC)
    public long leapS;

    // Validity Flags
    public long valid;

    // Time Accuracy Estimate
    public long tAcc;


    public long getGalTow() {
        return galTow;
    }

    public long getfGalTow() {
        return fGalTow;
    }

    public long getFullBiasNano() {
        return (galTow + (604800 * (galWno + 1024))) * E9;
    }


    @NonNull
    @Override
    public String toString() {
        return "NavTimegal iTOW = " + iTOW + " galTow = " + galTow + " fGalTow = " + fGalTow + " galWno = " + galWno +
                " leapS = " + leapS + " valid = " + valid + " tAcc = " + tAcc;
    }
}

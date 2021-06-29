package com.bq.goeasygnssdatalib.ubxdata;

import androidx.annotation.NonNull;

import java.util.Arrays;


/*
 * Clock Solution
 */
public class NavClock extends UbxData {


    // Sample message

    // header(2) class+id(2) length(2) iTOW(4)
    // B5 62     01 35       38 00     A0 E7 C9 17

    // version(1) numSvs(1) reserved1(2)
    // 01         04        00 00

    // gnssId(1) svId(1) cno(1) elev(2) azim(2) prRes(2) flags(4)
    // 00        0B      00     A5      00 00   00 00    01 00 00 00
    // 00        0C      00     A5      00 00   00 00    01 00 00 00
    // 00        18      17     A5      00 00   00 00    04 00 00 00
    // 00        19      18     A5      00 00   00 00    14 09 00 00

    // chs(2)
    // 08 46

    NavClock() {
        messageClass = UBX_NAV_CLOCK;
    }

    // GPS time of week of the navigation epoch
    public long iTOW;

    // Clock bias
    public long clkB;

    // Clock drift
    public long clkD;

    // Time accuracy estimate
    public long tAcc;

    // Frequency accuracy estimate
    public long fAcc;

    @NonNull
    @Override
    public String toString() {
        return "NavClock " + " iTOW = " + iTOW + " clkB = " + clkB + " clkD = " + clkD +
                " tAcc = " + tAcc + " fAcc = " + fAcc;
    }


}

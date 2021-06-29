package com.bq.goeasygnssdatalib.ubxdata;

import androidx.annotation.NonNull;

import java.util.Arrays;


/*
 * Satellite Information
 * This message displays information about SVs which are either known to be
 * visible or currently tracked by the receiver.
 */
public class NavSat extends UbxData {


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

    NavSat() {
        messageClass = UBX_NAV_SAT;
    }

    // GPS time of week of the navigation epoch
    public long iTOW;

    // Message version (1 for this version)
    public long version;

    // Number of satellites
    public long numSvs;

    // Reserved
    public long reserved1;

    // Satellite object
    public Sv[] svs;

    public static class Sv {

        // GNSS identifier
        public long gnssId;

        // Satellite identifier
        public long svId;

        // Carrier to noise ratio (signal strength)
        public long cno;

        // Elevation (range: +/-90), unknown if out of range
        public long elev;

        // Azimuth (range 0-360), unknown if elevation is out of range
        public long azim;

        // Pseudorange residual
        public long prRes;

        // Bitmask
        public long flags;

        @NonNull
        @Override
        public String toString() {
            return "SV gnssId = " + gnssId + " svId = " + svId + " cno = " + cno + " elev = " + elev +
                    " azim = " + azim + " prRes = " + prRes + " flags = " + flags;
        }

    }

    @NonNull
    @Override
    public String toString() {
        return "NavSat " + " iTOW = " + iTOW + " version = " + version + " numSvs = " + numSvs +
                " reserved1 = " + reserved1 + " svs = " + Arrays.toString(svs);
    }


}

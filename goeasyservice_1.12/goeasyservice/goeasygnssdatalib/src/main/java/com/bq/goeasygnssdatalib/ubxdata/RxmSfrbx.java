package com.bq.goeasygnssdatalib.ubxdata;

import androidx.annotation.NonNull;


/*
 * Broadcast Navigation Data Subframe
 * This message reports a complete subframe of broadcast navigation data
 * decoded from a single signal.
 */
public class RxmSfrbx extends UbxData {

    // Sample message

    // header(2) class+id(2) length(2) gnssId(1) svId(1) reserved1(1) freqId(1) numWords(1) reserved2(1) version(1) reserved3(1)
    // B5 62     02 13       30 00     00        19      00           00        0A          04           02         D6

    // dwrd(4*numWords)                                                                 CHS(2)
    // 302BC0229C0C792029F9491EC42498A67CF74114E3326C0AC9394E001D01BAAC48842881989A4F03 91 41

    public static final int GNSS_ID_GPS = 0;
    public static final int GNSS_ID_GALILEO = 2;
    public static final int GNSS_ID_GLONASS = 6;

    RxmSfrbx() {
        messageClass = UBX_RXM_SFRBX;
    }

    // GNSS identifier
    public int gnssId;

    // Satellite identifier
    public int svId;

    // Reserved
    public int reserved1;

    // Only used for GLONASS: This is the frequency slot + 7 (range from 0 to 13)
    public int freqId;

    // The number of data words contained in this message (0..16)
    public int numWords;

    // Reserved
    public int chn;

    // Message version (0x01 for this version)
    public int version;

    // Reserved
    public int reserved2;

    // The data words
    public String dwrd;


    public int getSvId() {
        return svId;
    }

    public String getDwrd() {
        return dwrd;
    }

    public int getGnssId() {
        return gnssId;
    }

    @NonNull
    @Override
    public String toString() {
        return "RxmSfrbx gnssId = " + gnssId + " svId = " + svId + " reserved1 = " + reserved1 + " freqId = " + freqId +
                " numWords = " + numWords + " chn = " + chn + " version = " + version
                + " reserved2 = " + reserved2 + " dwrd = " + dwrd;
    }
}

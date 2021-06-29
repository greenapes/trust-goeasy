package com.bq.goeasygnssdatalib.ubxdata;

import android.util.Log;

import static com.bq.goeasygnssdatalib.ubxdata.ParserUtils.calculateChecksum;
import static com.bq.goeasygnssdatalib.ubxdata.ParserUtils.hexToUnsignedLong;
import static com.bq.goeasygnssdatalib.ubxdata.UbxData.*;
import static com.bq.goeasygnssdatalib.ubxdata.ParserUtils.hexToSignedLong;
import static com.bq.goeasygnssdatalib.ubxdata.ParserUtils.hexToHexLittleEndian;


/*
 * Parser class. Takes the hexadecimal string messages and extracts all different fields into its corresponding classes
 */
public class UbxDataParser {

    private final static String TAG = "GOEASY_PARSER";

    public static UbxDataGroup parseGnssDataString(String message, UbxDataGroup ubxDataGroup) {

        String messageHeader = message.substring(0, 4);

        ubxDataGroup = new UbxDataGroup();

        ubxDataGroup.message = message;

        if (!messageHeader.equals(UBX_HEADER)) {
            Log.i(TAG, "Unknown message header " + messageHeader);
            Log.i(TAG, "RawMessage " + message);
            return ubxDataGroup;
        }

        return parseMessage(message, ubxDataGroup);
    }

    private static UbxDataGroup parseMessage(String message, UbxDataGroup ubxDataGroup) {
        String messageClass = message.substring(4, 8);
        String messageLength = hexToHexLittleEndian(message.substring(8, 12));
        String messagePayload = message.substring(12, 12 + 2 * Integer.parseInt(messageLength, 16));
        String rawHex = message.substring(0, 12 + 2 * Integer.parseInt(messageLength, 16) + 4);
        String nextMessage = message.substring(16 + 2 * Integer.parseInt(messageLength, 16));

//        if (!calculateChecksum(rawHex)) {
//            return null;
//        }

        switch (messageClass) {
            case UBX_RXM_SFRBX:
                ubxDataGroup.rxmSfrbxRaw.add(rawHex);
                ubxDataGroup.rxmSfrbx.add((RxmSfrbx) parseRxmSfrbx(messagePayload));
                break;
            case UBX_NAV_POSLLH:
                ubxDataGroup.navPosllhRaw = rawHex;
                ubxDataGroup.navPosllh = (NavPosllh) parseNavPosllh(messagePayload);
                break;
            case UBX_NAV_TIMEGAL:
                ubxDataGroup.navTimegalRaw = rawHex;
                ubxDataGroup.navTimegal = (NavTimegal) parseNavTimegal(messagePayload);
                break;
            case UBX_NAV_SAT:
                ubxDataGroup.navSatRaw = rawHex;
                ubxDataGroup.navSat = (NavSat) parseNavSat(messagePayload);
                break;
            case UBX_NAV_CLOCK:
                ubxDataGroup.navClockRaw = rawHex;
                ubxDataGroup.navClock = (NavClock) parseNavClock(messagePayload);
                break;
            default:
                Log.i(TAG, "Unknown message class " + messageClass);
                Log.i(TAG, "RawMessage " + message);
        }

        if (nextMessage.contains(UBX_HEADER)) {
            parseMessage(nextMessage.substring(nextMessage.indexOf(UBX_HEADER)), ubxDataGroup);
        }
        return ubxDataGroup;
    }


    // Sample data
    // header(2) class+id(2) length(2) gnssId(1) svId(1) reserved1(1) freqId(1) numWords(1) reserved2(1) version(1) reserved3(1)
    // B5 62     02 13       30 00     00        19      00           00        0A          04           02         D6
    // dwrd(4*numWords)                                                                 CHS(2)
    // 302BC0229C0C792029F9491EC42498A67CF74114E3326C0AC9394E001D01BAAC48842881989A4F03 91 41
    private static UbxData parseRxmSfrbx(String payload) {
        RxmSfrbx rxmSfrbx = new RxmSfrbx();

        rxmSfrbx.gnssId = Integer.parseInt(payload.substring(0, 2), 16);
        rxmSfrbx.svId = Integer.parseInt(payload.substring(2, 4), 16);
        rxmSfrbx.reserved1 = Integer.parseInt(payload.substring(4, 6), 16);
        rxmSfrbx.freqId = Integer.parseInt(payload.substring(6, 8), 16);
        rxmSfrbx.numWords = Integer.parseInt(payload.substring(8, 10), 16);
        rxmSfrbx.chn = Integer.parseInt(payload.substring(10, 12), 16);
        rxmSfrbx.version = Integer.parseInt(payload.substring(12, 14), 16);
        rxmSfrbx.reserved2 = Integer.parseInt(payload.substring(14, 16), 16);
        rxmSfrbx.dwrd = hexToHexLittleEndian(payload.substring(16, 16 + (2 * 4 * rxmSfrbx.numWords)));

        return rxmSfrbx;
    }


    // Sample data
    // header(2) class+id(2)    length(1) iTOW(4)        lon(4)         lat(4)         height(4)      hMSL(4)        hAcc(4)        vAcc(4)        CHS(2)
    // B5 62     01 02          1C 00     88 32 D3 17    62 A8 AE FD    2A E1 1E 18    50 7B 0C 00    2A B7 0B 00    B7 CC 31 00    A9 5E 2F 00    66 D3
    private static UbxData parseNavPosllh(String payload) {
        NavPosllh navPosllh = new NavPosllh();

        navPosllh.iTOW = hexToUnsignedLong(payload.substring(0, 8), 4);
        navPosllh.lon = hexToSignedLong(payload.substring(8, 16), 4);
        navPosllh.lat = hexToSignedLong(payload.substring(16, 24), 4);
        navPosllh.height = hexToSignedLong(payload.substring(24, 32), 4);
        navPosllh.hMSL = hexToSignedLong(payload.substring(32, 40), 4);
        navPosllh.hAcc = hexToUnsignedLong(payload.substring(40, 48), 4);
        navPosllh.vAcc = hexToUnsignedLong(payload.substring(48, 56), 4);

        return navPosllh;
    }

    // Sample data
    // header(2) class+id(2)    length(2) iTOW(4)        galTow(4)      fGalTow(4)     galWno(2)  leapS (1)  valid(1)  tAcc(4)        CHS(2)
    // B5 62     01 25          14 00     A0 E7 C9 17    04 17 06 00    CE 18 00 00    2B 04      11         03        10 3A 31 01    67 48
    public static UbxData parseNavTimegal(String payload) {
        NavTimegal navTimegal = new NavTimegal();

        navTimegal.iTOW = hexToUnsignedLong(payload.substring(0, 8), 4);
        navTimegal.galTow = hexToUnsignedLong(payload.substring(8, 16), 4);
        navTimegal.fGalTow = hexToSignedLong(payload.substring(16, 24), 4);
        navTimegal.galWno = hexToSignedLong(payload.substring(24, 28), 2);
        navTimegal.leapS = hexToSignedLong(payload.substring(28, 30), 1);
        navTimegal.valid = hexToSignedLong(payload.substring(30, 32), 1);
        navTimegal.tAcc = hexToUnsignedLong(payload.substring(32, 40), 4);

        return navTimegal;
    }

    //Sample data
    // header(2) class+id(2) length(2) iTOW(4)
    // B5 62     01 35       38 00     A0 E7 C9 17
    // version(1) numSvs(1) reserved1(2)
    // 01         04        00 00
    // gnssId(1) svId(1) cno(1) elev(1) azim(2) prRes(2) flags(4)
    // 00        0B      00     A5      00 00   00 00    01 00 00 00
    // 00        0C      00     A5      00 00   00 00    01 00 00 00
    // 00        18      17     A5      00 00   00 00    04 00 00 00
    // 00        19      18     A5      00 00   00 00    14 09 00 00
    // chs(2)
    // 08 46
    public static UbxData parseNavSat(String payload) {
        NavSat navSat = new NavSat();

        navSat.iTOW = hexToUnsignedLong(payload.substring(0, 8), 4);
        navSat.version = Integer.parseInt(payload.substring(8, 10), 16);
        navSat.numSvs = Integer.parseInt(payload.substring(10, 12), 16);
        navSat.reserved1 = hexToUnsignedLong(payload.substring(12, 16), 2);
        navSat.svs = new NavSat.Sv[(int) navSat.numSvs];

        int j;
        for (int i = 0; i < navSat.numSvs; i++) {
            j = 24 * i;
            navSat.svs[i] = new NavSat.Sv();
            navSat.svs[i].gnssId = Integer.parseInt(payload.substring(16 + j, 18 + j), 16);
            navSat.svs[i].svId = Integer.parseInt(payload.substring(18 + j, 20 + j), 16);
            navSat.svs[i].cno = Integer.parseInt(payload.substring(20 + j, 22 + j), 16);
            navSat.svs[i].elev = Integer.parseInt(payload.substring(22 + j, 24 + j), 16);
            navSat.svs[i].azim = hexToSignedLong(payload.substring(24 + j, 28 + j), 2);
            navSat.svs[i].prRes = hexToSignedLong(payload.substring(28 + j, 32 + j), 2);
            navSat.svs[i].flags = hexToSignedLong(payload.substring(32 + j, 40 + j), 4);
        }

        return navSat;
    }


    private static UbxData parseNavClock(String payload) {
        NavClock navClock = new NavClock();

        navClock.iTOW = hexToUnsignedLong(payload.substring(0, 8), 4);
        navClock.clkB = hexToSignedLong(payload.substring(8, 16), 4);
        navClock.clkD = hexToSignedLong(payload.substring(16, 24), 4);
        navClock.tAcc = hexToUnsignedLong(payload.substring(24, 32), 4);
        navClock.fAcc = hexToUnsignedLong(payload.substring(32, 40), 4);

        return navClock;
    }

}

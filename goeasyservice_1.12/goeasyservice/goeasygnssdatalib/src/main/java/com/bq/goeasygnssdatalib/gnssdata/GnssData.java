package com.bq.goeasygnssdatalib.gnssdata;

import android.location.GnssNavigationMessage;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bq.goeasygnssdatalib.ubxdata.RxmSfrbx;
import com.bq.goeasygnssdatalib.ubxdata.UbxDataGroup;
import com.bq.goeasygnssdatalib.ubxdata.ParserUtils;

import java.util.Arrays;

import static com.bq.goeasygnssdatalib.gnssdata.GnssDataUtils.getDataByteArray;
import static com.bq.goeasygnssdatalib.gnssdata.GnssDataUtils.getGalileoDataByteArray;
import static com.bq.goeasygnssdatalib.gnssdata.GnssDataUtils.formatGalileoDwrdIntArray;
import static com.bq.goeasygnssdatalib.gnssdata.GnssDataUtils.getMessageId;
import static com.bq.goeasygnssdatalib.gnssdata.GnssDataUtils.getSubMessageId;


/*
 * Class with all the required data fields from the ublox receiver
 */
public class GnssData {

    private static final String TAG = "GOEASY_GNSSDATA";

    // Data needed from this class
    //{"authenticity":-1,"galileo_auth":[{"type":-1,"galileo_auth":[],"time":0.0,"fullbiasnano":0.0,"timenano":0.0,"svid":0, "status":0, "msgid":0, "submsgid":0, data:[]}],"lat":0.0,"lon":0.0,"time":0}

    // Data from u-blox device
    UbxDataGroup ubxDataGroup;

    // needed by Rossen to identify validation status, start with -1
    int authenticity = -1;
    double lat = -1;
    double lon = -1;
    long time = -1;

    double TENTOMINUSSEVEN = 0.0000001;

    // An array of GNSS data composed by GNSS RAW and timenano and fullbiasnano obtained from GNSS Measure
    GalileoAuth[] galileoAuth;

    public GnssData(UbxDataGroup ubxDataGroup) {

        this.ubxDataGroup = ubxDataGroup;

        if (ubxDataGroup.navPosllh != null) {
            // According to Ubx documentation, lon and lat scaling is 1e-7
            lat = ubxDataGroup.navPosllh.getLat() * TENTOMINUSSEVEN;
            lon = ubxDataGroup.navPosllh.getLon() * TENTOMINUSSEVEN;
        }

        time = System.currentTimeMillis();

        if (ubxDataGroup.rxmSfrbx == null)
            return;

        galileoAuth = new GalileoAuth[ubxDataGroup.rxmSfrbx.size()];

        for (int i = 0; i < ubxDataGroup.rxmSfrbx.size(); i++) {
            galileoAuth[i] = new GalileoAuth();
            if (ubxDataGroup.navTimegal != null) {
                galileoAuth[i].fullbiasnano = ubxDataGroup.navTimegal.getFullBiasNano();
                galileoAuth[i].timenano = ubxDataGroup.navTimegal.getfGalTow();
                galileoAuth[i].msgid = getMessageId(ubxDataGroup.navTimegal.getGalTow());
                galileoAuth[i].submsgid = getSubMessageId(ubxDataGroup.navTimegal.getGalTow(), galileoAuth[i].msgid);
            }
            galileoAuth[i].time = System.currentTimeMillis();
            galileoAuth[i].svid = ubxDataGroup.rxmSfrbx.get(i).getSvId();
            galileoAuth[i].status = 1;

            // Depending on type, we have to format dwrd data in one way or another
            try {
                switch (ubxDataGroup.rxmSfrbx.get(i).getGnssId()) {
                    case RxmSfrbx.GNSS_ID_GALILEO:
                        galileoAuth[i].type = GnssNavigationMessage.TYPE_GAL_I;
                        galileoAuth[i].dataIntegers = formatGalileoDwrdIntArray(ubxDataGroup.rxmSfrbx.get(i).getDwrd());
                        galileoAuth[i].data = getGalileoDataByteArray(ubxDataGroup.rxmSfrbx.get(i).getDwrd());
                        break;
                    case RxmSfrbx.GNSS_ID_GPS:
                        galileoAuth[i].type = GnssNavigationMessage.TYPE_GPS_L1CA;
                        galileoAuth[i].dataIntegers = ParserUtils.getTwosComplementArray(ubxDataGroup.rxmSfrbx.get(i).getDwrd());
                        galileoAuth[i].data = getDataByteArray(ubxDataGroup.rxmSfrbx.get(i).getDwrd());
                        break;
                    case RxmSfrbx.GNSS_ID_GLONASS:
                        galileoAuth[i].type = GnssNavigationMessage.TYPE_GLO_L1CA;
                        galileoAuth[i].dataIntegers = ParserUtils.getTwosComplementArray(ubxDataGroup.rxmSfrbx.get(i).getDwrd());
                        galileoAuth[i].data = getDataByteArray(ubxDataGroup.rxmSfrbx.get(i).getDwrd());
                        break;
                }

            } catch (Exception e) {
                Log.e(TAG, "Exception when parsing dwrd data " + e);
                Log.e(TAG, "Raw dwrd: " + ubxDataGroup.rxmSfrbx.get(i).getDwrd());
            }

        }

    }

    public UbxDataGroup getUbxDataGroup() {
        return ubxDataGroup;
    }

    public int getAuthenticity() {
        return authenticity;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public long getTime() {
        return time;
    }

    public GalileoAuth[] getGalileoAuth() {
        return galileoAuth;
    }

    @NonNull
    @Override
    public String toString() {
        return "GnssData authenticity " + authenticity + " lat " + lat + " lon " + lon + " time " +
                time + " " + Arrays.toString(galileoAuth);
    }
}

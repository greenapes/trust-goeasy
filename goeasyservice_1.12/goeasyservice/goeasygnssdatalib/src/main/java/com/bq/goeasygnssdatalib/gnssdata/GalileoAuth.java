package com.bq.goeasygnssdatalib.gnssdata;

import androidx.annotation.NonNull;

import java.util.ArrayList;

/*** Created by lupobianco on 30/09/2020.*/

// An array of GNSS data composed by GNSS RAW and timenano and fullbiasnano obtained from GNSS Measure
public class GalileoAuth {
    int type = -1;
    long time = -1;
    long fullbiasnano = -1;
    long timenano = -1;
    int svid = -1;
    int status = -1;
    int msgid = -1;
    int submsgid = -1;
    ArrayList<Integer> dataIntegers;
    byte[] data;

    public int getType() {
        return type;
    }

    public long getTime() {
        return time;
    }

    public long getFullbiasnano() {
        return fullbiasnano;
    }

    public long getTimenano() {
        return timenano;
    }

    public int getSvid() {
        return svid;
    }

    public int getStatus() {
        return status;
    }

    public int getMsgid() {
        return msgid;
    }

    public int getSubmsgid() {
        return submsgid;
    }

    public ArrayList<Integer> getDataIntegers() {
        return dataIntegers;
    }

    public byte[] getData() {
        return data;
    }

    @NonNull
    @Override
    public String toString() {
        return "GalileoAuth type " + type + " time " + time + " fullbiasnano " + fullbiasnano +
                " timenano " + timenano + " svid " + svid + " status " + status + " msgid " + msgid +
                " submsgid " + submsgid + " dataIntegers " + dataIntegers + " data " + data;
    }

}
package com.bq.goeasygnssdatalib.gnssdata;

import androidx.annotation.VisibleForTesting;

import com.bq.goeasygnssdatalib.ubxdata.ParserUtils;
import com.google.common.io.BaseEncoding;

import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Math.floor;

public class GnssDataUtils {

    protected static int getMessageId(long galTow) {
        return (int) (floor(((galTow - 3) % 720) / 30.0) + 1);
    }

    protected static int[] subMessageIdValues = {2, 4, 6, 7, 8, 0, 0, 0, 0, 0, 1, 3, 5, 0, 0};

    protected static int getSubMessageId(long t0, int messageId) {
     //   int subMessageIdValueTemp = subMessageIdValues[(int) (floor(((t0 - 3) % 30) / 2.0) + 1)];
      //  if (messageId % 2 != 0) {
    //        if (subMessageIdValueTemp == 7)
    //            subMessageIdValueTemp = 9;
    //        else if (subMessageIdValueTemp == 8)
    //            subMessageIdValueTemp = 10;
    //    }

        int subMessageIdValueTemp = 0;
        return subMessageIdValueTemp;
    }

    // Checking dwrd data conversion from hex string to two's complement, and removing unnecessary bytes
    @VisibleForTesting
    protected static ArrayList<Integer> formatGalileoDwrdIntArray(String dwrd) {
        // Removing last word, as u-center does
        String data = dwrd.substring(0, dwrd.length() - 8);
        ArrayList<Integer> dataArray = ParserUtils.getTwosComplementArray(data);
        // Removing bytes 16 and 32 (they are padding which should be always zero)
        dataArray.remove(15);
        dataArray.remove(30);
        return dataArray;
    }

    @VisibleForTesting
    protected static byte[] getGalileoDataByteArray(String dwrd) {
        // Removing last word, as u-center does
        String dataString = dwrd.substring(0, dwrd.length() - 8);
        byte[] data = BaseEncoding.base16().lowerCase().decode(dataString.toLowerCase());
        // Removing bytes 16 and 32 (they are padding which should be always zero)
        byte[] dataStart = Arrays.copyOfRange(data, 0, 15);
        byte[] dataEnd = Arrays.copyOfRange(data, 16, 31);
        byte[] result = new byte[dataStart.length + dataEnd.length];

        System.arraycopy(dataStart, 0, result, 0, dataStart.length);
        System.arraycopy(dataEnd, 0, result, 15, dataEnd.length);
        return result;
    }

    @VisibleForTesting
    protected static byte[] getDataByteArray(String dwrd) {
        byte[] data = BaseEncoding.base16().lowerCase().decode(dwrd.toLowerCase());
        return data;
    }

}

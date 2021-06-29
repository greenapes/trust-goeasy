package com.bq.goeasygnssdatalib.ubxdata;

import android.os.Environment;
import android.util.Log;

import androidx.annotation.VisibleForTesting;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.util.ArrayList;

public class ParserUtils {

    private static final String TAG = "GOEASY_UTILS";

    // Converts a hexadecimal string to its twoscomplement signed long
    public static long hexToSignedLong(final String hex, int bytes) {
        return getTwosComplement(hexToBin(hexToHexLittleEndian(hex), bytes));
    }

    public static long hexToUnsignedLong(final String hex, int bytes) {
        return Long.parseLong((hexToBin(hexToHexLittleEndian(hex), bytes)), 2);
    }

    // Converts a hexadecimal string to an array of twoscomplement integers
    @VisibleForTesting
    public static ArrayList<Integer> getTwosComplementArray(final String hexString) {
        ArrayList<Integer> result = new ArrayList<>();
        for (int i = 0; i < hexString.length(); i += 2) {
            result.add((int) getTwosComplement(hexToBin(hexString.substring(i, i + 2), 8)));
        }
        return result;
    }

    public static String hexToHexLittleEndian(final String hexString) {
        StringBuilder hexLittleEndian = new StringBuilder();
        if (hexString.length() > 8) {
            for (int i = 0; i < hexString.length(); i += 8) {
                hexLittleEndian.append(hexToHexLittleEndian(hexString.substring(i, i + 8)));
            }
        } else {
            for (int i = hexString.length() - 2; i >= 0; i -= 2) {
                hexLittleEndian.append(hexString.substring(i, i + 2));
            }
        }
        return hexLittleEndian.toString();
    }

    public static String hexToBin(String s, int bytes) {
        String preBin = new BigInteger(s, 16).toString(2);
        int length = preBin.length();
        int bits = bytes * 8;
        if (length < bits) {
            for (int i = 0; i < bits - length; i++) {
                preBin = "0" + preBin;
            }
        }
        return preBin;
    }

    static long getTwosComplement(String bin) {
        if (bin.charAt(0) == '1') {
            String inverted = invertDigits(bin);
            long decimalValue = Integer.parseInt(inverted, 2);
            decimalValue = (decimalValue + 1) * -1;
            return decimalValue;
        } else {
            return Long.parseLong(bin, 2);
        }
    }

    static String invertDigits(String binaryInt) {
        String result = binaryInt;
        result = result.replace("0", " ");
        result = result.replace("1", "0");
        result = result.replace(" ", "1");
        return result;
    }


    public static void writeToFile(String message, String data, String filename) {
        writeStringToFile("RawMessage \n" + message + "\n\n", filename);
        if (data != null) {
            writeStringToFile(data + "\n\n", filename);
            Log.i(TAG, data);
        } else {
            writeStringToFile("Couldn't parse message", filename);
            Log.i(TAG, "Couldn't parse message");
        }
    }


    public static void writeStringToFile(String data, String filename) {

        final File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS);
        if (!path.exists()) {
            path.mkdirs();
        }
        final File file = new File(path, filename);
        try {
            file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file, true);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(data);
            myOutWriter.close();
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            Log.e(TAG, "File write failed: " + e.toString());
        }
    }

    static boolean calculateChecksum(String rawHex) {
        String checksumBase = rawHex.substring(4, rawHex.length() - 4);
        String checksum = rawHex.substring(rawHex.length() - 4);
        int chA = 0;
        int chB = 0;
        for (int i = 0; i < checksumBase.length() / 2; i++) {
            chA += Integer.parseInt(checksumBase.substring(i * 2, i * 2 + 2), 16);
            chB += chA;
        }
        String chAStringFull = Integer.toHexString(chA);
        String chBStringFull = Integer.toHexString(chB);
        String chAString = chAStringFull.substring(chAStringFull.length() - 2);
        String chBString = chBStringFull.substring(chBStringFull.length() - 2);
        if (chAString.equalsIgnoreCase(checksum.substring(0, 2)) && chBString.equalsIgnoreCase(checksum.substring(2, 4))) {
            Log.i(TAG, "Checksum = " + checksum + " Result = " + chAString + chBString);
            return true;
        } else {
            Log.e(TAG, "Checksum = " + checksum + " Result = " + chAString + chBString);
            return false;
        }
    }

}

package com.tmp.smartthings.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * Created by phapli on 17/05/2016.
 */
public class CommonUtil {
    private static CommonUtil ourInstance = new CommonUtil();

    public static CommonUtil getInstance() {
        return ourInstance;
    }

    private CommonUtil() {
    }

    public byte[] intToByte(int input){
        byte[] value = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(input).array();
        return Arrays.copyOfRange(value, 0, 2);
    }

    public int byteToInt(byte[] input){
        byte[] new_bytes = {0x00, 0x00, 0x00, 0x00};
        System.arraycopy(input, 0, new_bytes, 3, 1);
        System.arraycopy(input, 1, new_bytes, 2, 1);
        return ByteBuffer.wrap(new_bytes).getInt();
    }

    public String byteToHex(byte[] input){
        final StringBuilder stringBuilder = new StringBuilder(input.length);
        for (byte byteChar : input)
            stringBuilder.append(String.format("%02x", byteChar));
        return stringBuilder.toString();
    }

    public String byteToHexWithSpaceFormat(byte[] input){
        final StringBuilder stringBuilder = new StringBuilder(input.length);
        for (byte byteChar : input)
            stringBuilder.append(String.format("%02X ", byteChar));
        return stringBuilder.toString();
    }

    public byte[] reverse(byte[] input){
        for(int i = 0; i < input.length / 2; i++)
        {
            byte temp = input[i];
            input[i] = input[input.length - i - 1];
            input[input.length - i - 1] = temp;
        }
        return input;
    }

}

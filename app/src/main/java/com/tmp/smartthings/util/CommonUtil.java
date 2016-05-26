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
        System.arraycopy(input, 0, new_bytes, 2, 2);
        return ByteBuffer.wrap(new_bytes).getInt();
    }

}

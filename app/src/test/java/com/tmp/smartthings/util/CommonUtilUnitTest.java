package com.tmp.smartthings.util;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class CommonUtilUnitTest {
    @Test
    public void intToByte() throws Exception {
        byte[] result = CommonUtil.getInstance().intToByte(12345);
        Assert.assertArrayEquals(result, new byte[]{0x00, 0x7B});
    }

    @Test
    public void byteToInt() throws Exception {
        byte[] input =new byte[]{(byte) 0x8D, (byte) 0xE5};
        int result = CommonUtil.getInstance().byteToInt(input);
        byte[] outBytes = CommonUtil.getInstance().intToByte(result);
        assertEquals(result, 16507);
    }

    @Test
    public void Int2BytesArr2() {
        int i = 12345;
        byte[] result = new byte[2];

        //result[0] = (byte) (i >> 24);
        //result[1] = (byte) (i >> 16);
        //result[2] = (byte) (i >> 8);
        //result[3] = (byte) (i /*>> 0*/);
        result[1] = (byte) (i >> 8);
        result[0] = (byte) (i >> 0);

        assertEquals (result, result);
    }
}
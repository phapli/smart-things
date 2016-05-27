/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tmp.smartthings.util;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.util.Log;

import com.tmp.smartthings.model.Result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class includes a small subset of standard GATT attributes for demonstration purposes.
 */
public class GattUtil {
    private static final String TAG = GattUtil.class.getName();
    private static HashMap<String, String> attributes = new HashMap();

    // Service
    public static final String CONTROL_SERVICE = "0000ffa0-0000-1000-8000-00805f9b34fb";
    public static List<String> services = new ArrayList<>();
    static {
        services.add(CONTROL_SERVICE);
    }

    // Characteristic
    public static final String SWITCH_CHAR = "0000ffa1-0000-1000-8000-00805f9b34fb";
    public static final String AUTH_PIN_CHAR = "0000ffb0-0000-1000-8000-00805f9b34fb";
    public static final String GET_BLACK_LIST_CHAR = "0000ffa7-0000-1000-8000-00805f9b34fb";
    public static final String GET_CONNECTED_LIST_CHAR = "0000ffa9-0000-1000-8000-00805f9b34fb";
    public static final String GET_WHITE_LIST_CHAR = "0000ffa6-0000-1000-8000-00805f9b34fb";
    public static final String GEN_PIN_CHAR = "0000ffab-0000-1000-8000-00805f9b34fb";
    public static final String REQ_OWNER_RIGHT_CHAR = "0000ffae-0000-1000-8000-00805f9b34fb";
    public static final String CHANGE_OWNER_PIN_CHAR = "0000ffaf-0000-1000-8000-00805f9b34fb";
    public static List<String> characteristics = new ArrayList<>();
    static {
        characteristics.add(SWITCH_CHAR);
        characteristics.add(AUTH_PIN_CHAR);
        characteristics.add(GET_BLACK_LIST_CHAR);
        characteristics.add(GET_CONNECTED_LIST_CHAR);
        characteristics.add(GET_WHITE_LIST_CHAR);
        characteristics.add(GEN_PIN_CHAR);
        characteristics.add(REQ_OWNER_RIGHT_CHAR);
        characteristics.add(CHANGE_OWNER_PIN_CHAR);
    }

    private static GattUtil ourInstance = new GattUtil();

    public static GattUtil getInstance() {
        return ourInstance;
    }

    private GattUtil() {
    }

    public static String lookup(String uuid, String defaultName) {
        String name = attributes.get(uuid);
        return name == null ? defaultName : name;
    }

    public Result discoverGatt(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return new Result(1, "gatt is null");
        String uuid = null;
        HashMap<String, BluetoothGattCharacteristic> mGattCharacteristicMap = new HashMap<String, BluetoothGattCharacteristic>();

        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            uuid = gattService.getUuid().toString();
            List<BluetoothGattCharacteristic> gattCharacteristics =
                    gattService.getCharacteristics();
            if (services.contains(uuid)) {
                // Loops through available Characteristics.
                for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                    uuid = gattCharacteristic.getUuid().toString();
                    if (characteristics.contains(uuid)) {
                        Log.d(TAG, "support: " + uuid);
                        mGattCharacteristicMap.put(uuid, gattCharacteristic);
                    }
                }
            }
        }
        Result result;
        if(mGattCharacteristicMap.size()==characteristics.size()) {
            result = new Result(0, "success");
            result.setData("gatt_map", mGattCharacteristicMap);
        }else {
            result = new Result(2, "not enough required characteristics");
        }
        return result;
    }

    public String shortName(String uuid) {
        if(uuid!=null && uuid.length()>8){
            return uuid.substring(4,8);
        }
        return "NG";
    }
}

package com.tmp.smartthings.util;

import com.activeandroid.query.Select;
import com.tmp.smartthings.model.Device;

import java.util.List;

/**
 * Created by phapli on 17/05/2016.
 */
public class DeviceUtil {
    private static DeviceUtil ourInstance = new DeviceUtil();

    public static DeviceUtil getInstance() {
        return ourInstance;
    }

    private DeviceUtil() {
    }

    public List<Device> getAll() {
        return new Select()
                .from(Device.class)
                .execute();
    }

    public boolean isExisted(String address) {
        return new Select()
                .from(Device.class)
                .where("action = ?", address)
                .count() > 0;
    }

    public Device get(String address) {
        return new Select()
                .from(Device.class)
                .where("action = ?", address)
                .executeSingle();
    }
}

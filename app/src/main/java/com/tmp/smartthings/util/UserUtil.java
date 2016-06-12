package com.tmp.smartthings.util;

import com.activeandroid.query.Select;
import com.tmp.smartthings.model.Device;
import com.tmp.smartthings.model.User;

import java.util.List;

/**
 * Created by phapli on 17/05/2016.
 */
public class UserUtil {
    private static UserUtil ourInstance = new UserUtil();

    public static UserUtil getInstance() {
        return ourInstance;
    }

    private UserUtil() {
    }

    public List<User> getAll(String device_add) {
        return new Select()
                .from(User.class)
                .where("device_address = ?", device_add)
                .execute();
    }

    public boolean isExisted(String address, String device_add) {
        return new Select()
                .from(User.class)
                .where("action = ?", address)
                .and("device_address = ?", device_add)
                .count() > 0;
    }

    public Device get(String device_add, String address) {
        return new Select()
                .from(User.class)
                .where("action = ?", address)
                .and("device_address = ?", device_add)
                .executeSingle();
    }
}

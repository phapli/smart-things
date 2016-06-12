package com.tmp.smartthings.util;

import com.activeandroid.query.Select;
import com.tmp.smartthings.model.Device;
import com.tmp.smartthings.model.ActionLog;

import java.util.List;

/**
 * Created by phapli on 17/05/2016.
 */
public class ActionLogUtil {
    private static ActionLogUtil ourInstance = new ActionLogUtil();

    public static ActionLogUtil getInstance() {
        return ourInstance;
    }

    private ActionLogUtil() {
    }

    public List<ActionLog> getAll(String device_add) {
        return new Select()
                .from(ActionLog.class)
                .where("device_address = ?", device_add)
                .execute();
    }

}

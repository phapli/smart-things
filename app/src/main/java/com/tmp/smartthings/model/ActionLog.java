package com.tmp.smartthings.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by phapli on 17/05/2016.
 */
@Table(name = "ActionLog")
public class ActionLog extends Model{

    @Column(name = "address", index = true)
    private String address;

    @Column(name = "device_address", index = true)
    private String device_address;

    @Column(name = "action")
    private String action;

    @Column(name = "action_time")
    private long action_time;

    public ActionLog() {
        super();
    }

    public ActionLog(String device_address, String address, String action, long action_time) {
        this.device_address = device_address;
        this.address = address;
        this.action = action;
        this.action_time = action_time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public long getAction_time() {
        return action_time;
    }

    public void setAction_time(long action_time) {
        this.action_time = action_time;
    }

    public String getDevice_address() {
        return device_address;
    }

    public void setDevice_address(String device_address) {
        this.device_address = device_address;
    }
}

package com.tmp.smartthings.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.orm.SugarRecord;
import com.orm.dsl.Unique;

/**
 * Created by phapli on 17/05/2016.
 */
@Table(name = "Device")
public class Device extends Model{
    public enum Device_Type {
        LIGHT_SWITCH, DOOR_LOCK
    }

    @Column(name = "action", index = true, unique = true)
    private String address;

    @Column(name = "name")
    private String name;

    @Column(name = "pin")
    private int pin;

    @Column(name = "device_type")
    private Device_Type device_type;

    @Column(name = "position")
    private int position;

    @Column(name = "last_use")
    private long last_use;

    @Column(name = "is_admin")
    private boolean is_admin;

    public Device() {
        super();
    }

    public Device(String name, String address, Device_Type device_type, int pin, int position, long last_use) {
        super();
        this.address = address;
        this.name = name;
        this.device_type = device_type;
        this.pin = pin;
        this.position = position;
        this.last_use = last_use;
        this.is_admin = false;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Device_Type getDevice_type() {
        return device_type;
    }

    public void setDevice_type(Device_Type device_type) {
        this.device_type = device_type;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public long getLast_use() {
        return last_use;
    }

    public void setLast_use(long last_use) {
        this.last_use = last_use;
    }

    public boolean is_admin() {
        return is_admin;
    }

    public void setIs_admin(boolean is_admin) {
        this.is_admin = is_admin;
    }
}

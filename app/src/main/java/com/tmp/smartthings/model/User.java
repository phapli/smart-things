package com.tmp.smartthings.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by phapli on 17/05/2016.
 */
@Table(name = "User")
public class User extends Model{
    public enum User_Type {
        LIGHT_SWITCH, DOOR_LOCK
    }

    @Column(name = "address", index = true)
    private String address;

    @Column(name = "device_address", index = true)
    private String device_address;

    @Column(name = "name")
    private String name;

    @Column(name = "user_type")
    private User_Type user_type;

    public User() {
        super();
    }

    public User(String address, String device_address, String name, User_Type user_type) {
        this.address = address;
        this.device_address = device_address;
        this.name = name;
        this.user_type = user_type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDevice_address() {
        return device_address;
    }

    public void setDevice_address(String device_address) {
        this.device_address = device_address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User_Type getUser_type() {
        return user_type;
    }

    public void setUser_type(User_Type user_type) {
        this.user_type = user_type;
    }
}

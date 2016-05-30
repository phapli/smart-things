package com.tmp.smartthings.service;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;

/**
 * Created by phapli on 30-May-16.
 */
public class BluetoothCommand {
    public enum CommandType{
        CharacteristicRead, CharacteristicWrite, DescriptorRead, DescriptorWrite
    }

    private CommandType type;
    private BluetoothGattCharacteristic characteristic;
    private BluetoothGattDescriptor descriptor;

    public BluetoothCommand(CommandType type, BluetoothGattCharacteristic characteristic) {
        this.type = type;
        this.characteristic = characteristic;
        this.descriptor = null;
    }

    public BluetoothCommand(CommandType type, BluetoothGattDescriptor descriptor) {
        this.type = type;
        this.characteristic = null;
        this.descriptor = descriptor;
    }

    public CommandType getType() {
        return type;
    }

    public void setType(CommandType type) {
        this.type = type;
    }

    public BluetoothGattCharacteristic getCharacteristic() {
        return characteristic;
    }

    public void setCharacteristic(BluetoothGattCharacteristic characteristic) {
        this.characteristic = characteristic;
    }

    public BluetoothGattDescriptor getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(BluetoothGattDescriptor descriptor) {
        this.descriptor = descriptor;
    }
}

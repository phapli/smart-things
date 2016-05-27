package com.tmp.smartthings.view.activity;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.orm.SugarContext;
import com.tmp.smartthings.R;
import com.tmp.smartthings.model.Device;
import com.tmp.smartthings.model.Result;
import com.tmp.smartthings.service.BluetoothLeService;
import com.tmp.smartthings.util.CommonUtil;
import com.tmp.smartthings.util.DeviceUtil;
import com.tmp.smartthings.util.GattUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class DeviceControlActivity extends AppCompatActivity {

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    public static final String EXTRAS_DEVICE_PIN = "DEVICE_PIN";
    public static final String EXTRAS_DEVICE_NEW = "DEVICE_NEW";

    private static final String TAG = DeviceControlActivity.class.getName();
    private FloatingActionMenu mFabMenu;
    private ImageView mSwitch;
    private BluetoothLeService mBluetoothLeService;
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics;
    private TextView mDataText;
    private byte[] deviceCurrentStatus;

    private boolean mConnected;
    private CommonUtil mCommonUtil = CommonUtil.getInstance();
    private GattUtil mGattUtil = GattUtil.getInstance();
    private DeviceUtil mDeviceUtil = DeviceUtil.getInstance();
    private Map<String, BluetoothGattCharacteristic> mGattCharacteristicsMap;
    private boolean mNew;
    private FloatingActionButton mFabEditName;
    private FloatingActionButton mFabChangePass;
    private FloatingActionButton mFabAddUser;
    private Device mDevice;
    private FloatingActionButton mFabDeleteDevice;
    private EditText oldPinInput;
    private EditText newPinInput;
    private View positiveAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActiveAndroid.initialize(this);
        final Intent intent = getIntent();
        String mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        String mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);
        int mPin = intent.getIntExtra(EXTRAS_DEVICE_PIN, -1);
        mNew = intent.getBooleanExtra(EXTRAS_DEVICE_NEW, false);

        if (mNew) {
            mDevice = new Device(mDeviceName, mDeviceAddress, Device.Device_Type.LIGHT_SWITCH, mPin, 0, new Date().getTime());
        } else {
            mDevice = mDeviceUtil.get(mDeviceAddress);
            mDevice.setLast_use(new Date().getTime());
            mDevice.save();
        }

        setContentView(R.layout.activity_device_control);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final TextView mTitle = (TextView) toolbar.findViewById(R.id.tv_title);
        mTitle.setText(mDeviceName);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mFabMenu = (FloatingActionMenu) findViewById(R.id.fab_device_control_menu);
        mFabEditName = (FloatingActionButton) findViewById(R.id.fab_device_control_edit_name);
        mFabAddUser = (FloatingActionButton) findViewById(R.id.fab_device_control_add_user);
        mFabChangePass = (FloatingActionButton) findViewById(R.id.fab_device_control_change_pass);
        mFabDeleteDevice = (FloatingActionButton) findViewById(R.id.fab_device_control_delete_device);
        mSwitch = (ImageView) findViewById(R.id.iv_device_control_switch);
        mDataText = (TextView) findViewById(R.id.tv_control_device_data);

        mSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                byte[] value = {0x00, 0x00};
                if (deviceCurrentStatus != null) {
                    value = deviceCurrentStatus;
                }
                if (value[1] == 0x00) {
                    for (int i = 0; i < value.length; i++) {
                        value[i] = 0x01;
                    }
                } else {
                    for (int i = 0; i < value.length; i++) {
                        value[i] = 0x00;
                    }
                }
                mBluetoothLeService.writeCharacteristic(mGattCharacteristicsMap.get(GattUtil.SWITCH_CHAR), value);
                mFabMenu.close(true);
            }
        });

        mFabEditName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(DeviceControlActivity.this)
                        .title(mDevice.getName())
                        .content(R.string.input_name_content)
                        .inputType(InputType.TYPE_CLASS_TEXT)
                        .inputRange(2, 20)
                        .negativeText(android.R.string.cancel)
                        .positiveText(android.R.string.ok)
                        .input(R.string.input_name_hint, R.string.input_pin_prefill, false, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                mDevice.setName(input.toString());
                                mDevice.save();
                                mTitle.setText(input.toString());
                            }
                        }).show();
                mFabMenu.close(true);
            }
        });

        mFabDeleteDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(DeviceControlActivity.this)
                        .title(R.string.confirm_delete_title)
                        .content(R.string.confirm_delete)
                        .positiveText(android.R.string.ok)
                        .negativeText(android.R.string.cancel)
                        .onAny(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                if (DialogAction.valueOf(which.name()).equals(DialogAction.POSITIVE)) {
                                    mDevice.delete();
                                    mBluetoothLeService.disconnect();
                                    finish();
                                }
                            }
                        })
                        .show();
                mFabMenu.close(true);
            }
        });

        mFabChangePass.setOnClickListener(new View.OnClickListener() {
            public boolean isOldPinView;
            public boolean isNewPinView;

            @Override
            public void onClick(View v) {
                MaterialDialog dialog = new MaterialDialog.Builder(DeviceControlActivity.this)
                        .title(R.string.change_pin)
                        .customView(R.layout.dialog_change_pass, true)
                        .positiveText(android.R.string.ok)
                        .negativeText(android.R.string.cancel)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                //
                                int oldPin;
                                int newPin;
                                try {
                                    oldPin = Integer.valueOf(oldPinInput.getText().toString());
                                    newPin = Integer.valueOf(newPinInput.getText().toString());
                                    if (oldPin == mDevice.getPin()) {
                                        mBluetoothLeService.writeCharacteristic(mGattCharacteristicsMap.get(GattUtil.CHANGE_OWNER_PIN_CHAR), mCommonUtil.intToByte(newPin));
                                        mDevice.setPin(newPin);
                                        mDevice.save();
                                        return;
                                    }
                                } catch (Exception e) {
                                }
                                Toast.makeText(DeviceControlActivity.this, "Wrong pin", Toast.LENGTH_SHORT).show();
                            }
                        }).build();

                positiveAction = dialog.getActionButton(DialogAction.POSITIVE);
                //noinspection ConstantConditions
                oldPinInput = (EditText) dialog.getCustomView().findViewById(R.id.ed_old_pin);
                newPinInput = (EditText) dialog.getCustomView().findViewById(R.id.ed_new_pin);
                oldPinInput.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        positiveAction.setEnabled(s.toString().trim().length() == 5 && newPinInput.getText().toString().length() == 5);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });

                newPinInput.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        positiveAction.setEnabled(s.toString().trim().length() == 5 && oldPinInput.getText().toString().length() == 5);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });

                // Toggling the show password CheckBox will mask or unmask the password input EditText
                final ImageView showOldPin = (ImageView) dialog.getCustomView().findViewById(R.id.iv_show_old_pin);
                showOldPin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        oldPinInput.setInputType(!isOldPinView ? InputType.TYPE_NUMBER_VARIATION_PASSWORD | InputType.TYPE_NUMBER_FLAG_SIGNED : InputType.TYPE_NUMBER_VARIATION_NORMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
                        oldPinInput.setTransformationMethod(!isOldPinView ? PasswordTransformationMethod.getInstance() : null);
                        showOldPin.setImageResource(!isOldPinView ? R.drawable.ic_eye_grey600_18dp : R.drawable.ic_eye_off_grey600_18dp);
                        isOldPinView = !isOldPinView;
                    }
                });

                final ImageView showNewPin = (ImageView) dialog.getCustomView().findViewById(R.id.iv_show_new_pin);
                showNewPin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        newPinInput.setInputType(!isNewPinView ? InputType.TYPE_NUMBER_VARIATION_PASSWORD : InputType.TYPE_NUMBER_VARIATION_NORMAL);
                        newPinInput.setTransformationMethod(!isNewPinView ? PasswordTransformationMethod.getInstance() : null);
                        showNewPin.setImageResource(!isNewPinView ? R.drawable.ic_eye_grey600_18dp : R.drawable.ic_eye_off_grey600_18dp);
                        isNewPinView = !isNewPinView;
                    }
                });

                dialog.show();
                positiveAction.setEnabled(false); // disabled by default
                mFabMenu.close(true);
            }
        });

        mFabAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBluetoothLeService.readCharacteristic(mGattCharacteristicsMap.get(GattUtil.GEN_PIN_CHAR));
                mFabMenu.close(true);
            }
        });

        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDevice.getAddress());
            Log.d(TAG, "Connect request result=" + result);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBluetoothLeService.disconnect();
        unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        mBluetoothLeService = null;
        stopDisconnectTimer();
        SugarContext.terminate();
    }

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(mDevice.getAddress());
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    private boolean mSaved = false;
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
                Toast.makeText(DeviceControlActivity.this, "CONNECTED", Toast.LENGTH_SHORT).show();
                invalidateOptionsMenu();
                resetDisconnectTimer();
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
                Toast.makeText(DeviceControlActivity.this, "DISCONNECTED", Toast.LENGTH_SHORT).show();
                finish();
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // check All service support and authenticate
                Result result = mGattUtil.discoverGatt(mBluetoothLeService.getSupportedGattServices());
                Toast.makeText(DeviceControlActivity.this, result.message, Toast.LENGTH_SHORT).show();
                if (result.status == 0) {
                    mGattCharacteristicsMap = (Map<String, BluetoothGattCharacteristic>) result.getData("gatt_map");
                    mBluetoothLeService.writeCharacteristic(mGattCharacteristicsMap.get(GattUtil.AUTH_PIN_CHAR), mCommonUtil.intToByte(mDevice.getPin()));
                } else {
                    finish();
                }
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                if (!mSaved && mNew) {
                    long id = mDevice.save();
                    Toast.makeText(DeviceControlActivity.this, "save device: " + id, Toast.LENGTH_SHORT).show();
                    mSaved = true;
                }

                byte[] data = intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA);
                String uuid = intent.getStringExtra(BluetoothLeService.EXTRA_UUID);

                if (data != null) {
                    switch (uuid) {
                        case GattUtil.SWITCH_CHAR:
                            deviceCurrentStatus = data;
                            updateSwitchStage(data[1] == 0x01);
                            break;
                        case GattUtil.GEN_PIN_CHAR:
                            processGenPinData(data);
                            break;
                        case GattUtil.GET_CONNECTED_LIST_CHAR:
                            processLogData(data);
                            break;
                    }
                    displayData(uuid, data);
                }
            }
        }
    };

    private void processLogData(byte[] data) {
        Toast.makeText(this, "Log: " + mCommonUtil.byteToHex(data), Toast.LENGTH_SHORT).show();
    }

    private void processGenPinData(byte[] data) {
        if (data.length != 2) {
            Toast.makeText(this, "Fail to generate new Pin", Toast.LENGTH_SHORT).show();
            return;
        }
        new MaterialDialog.Builder(DeviceControlActivity.this)
                .title(R.string.add_user)
                .content("pin: " + mCommonUtil.byteToInt(data) + "\nNow, you must disconnect on this device for another device access.")
                .positiveText(R.string.disconnected)
                .negativeText(R.string.late)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        mBluetoothLeService.disconnect();
                    }
                })
                .show();
    }


    private void updateSwitchStage(boolean enable) {
        if (enable) {
            mSwitch.setImageResource(R.drawable.light_bulb_green);
        } else {
            mSwitch.setImageResource(R.drawable.light_bulb_gray);
        }
    }

    private void displayData(String uuid, byte[] data) {
        if (data.length > 0) {
            mDataText.setText(mGattUtil.shortName(uuid) + ": " + mCommonUtil.byteToHex(data));
        }
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    public static final long DISCONNECT_TIMEOUT = 3000; // 3s = 3 * 1000 ms

    private Handler disconnectHandler = new Handler() {
        public void handleMessage(Message msg) {
        }
    };

    private Runnable disconnectCallback = new Runnable() {
        @Override
        public void run() {
            mBluetoothLeService.disconnect();
        }
    };

    public void resetDisconnectTimer() {
        disconnectHandler.removeCallbacks(disconnectCallback);
        //disconnectHandler.postDelayed(disconnectCallback, DISCONNECT_TIMEOUT);
    }

    public void stopDisconnectTimer() {
        disconnectHandler.removeCallbacks(disconnectCallback);
    }

    @Override
    public void onUserInteraction() {
        resetDisconnectTimer();
    }

}

package com.tmp.smartthings.view.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelUuid;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.clans.fab.FloatingActionButton;
import com.orm.SugarContext;
import com.tmp.smartthings.R;
import com.tmp.smartthings.model.Device;
import com.tmp.smartthings.util.CommonUtil;
import com.tmp.smartthings.util.DeviceUtil;
import com.tmp.smartthings.util.GattUtil;
import com.tmp.smartthings.view.adapter.ScanDeviceAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class ScanDeviceActivity extends ActionBarActivity {

    private ListView mListView;
    private FloatingActionButton mFabReload;
    private ScanDeviceAdapter mAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private Handler mHandler;
    private static final String TAG = ScanDeviceActivity.class.getName();

    private static final int REQUEST_ENABLE_BT = 1;
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;
    private TextView mTitle;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private BluetoothLeScanner mBluetoothLeScanner;
    private ArrayList<ScanFilter> mScanFilters;
    private ScanSettings mScanSettings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_device);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mTitle = (TextView) toolbar.findViewById(R.id.tv_title);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mHandler = new Handler();

        mListView = (ListView) findViewById(R.id.lv_scan_device);

        mAdapter = new ScanDeviceAdapter(this);
        mAdapter.updateDevices(new ArrayList<Device>());

        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showInputDialog(mAdapter.getItem(position));
            }
        });

        mFabReload = (FloatingActionButton) findViewById(R.id.fab_scan_device_reload);
        mFabReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdapter.clear();
                scanLeDevice(!mScanning);
            }
        });


        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
            mScanSettings = new ScanSettings.Builder()
//                    .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
//                    .setReportDelay(0)
                    .build();
            mScanFilters = new ArrayList<>();
            ScanFilter sCanFilter = new ScanFilter.Builder().setServiceUuid(new ParcelUuid(UUID.fromString(GattUtil.CONTROL_SERVICE))).build();
            mScanFilters.add(sCanFilter);
            mScanCallback = new ScanCallback()
            {
                @Override
                public void onScanResult(int callbackType, ScanResult result)
                {
                    Log.i("callbackType", String.valueOf(callbackType));
                    final BluetoothDevice device = result.getDevice();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!mDeviceUtil.isExisted(device.getAddress())) {
                                mAdapter.addDevice(new Device(device.getName(), device.getAddress(), Device.Device_Type.LIGHT_SWITCH, 0, 0, new Date().getTime()));
//                        Toast.makeText(ScanDeviceActivity.this, mCommonUtil.byteToHexWithSpaceFormat(Arrays.copyOfRange(scanRecord, 11, 17)), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

                @Override
                public void onBatchScanResults(List<ScanResult> results)
                {
                    for (ScanResult sr : results)
                    {
                        Log.i("Scan Item: ", sr.toString());
                        final BluetoothDevice device = sr.getDevice();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!mDeviceUtil.isExisted(device.getAddress())) {
                                    mAdapter.addDevice(new Device(device.getName(), device.getAddress(), Device.Device_Type.LIGHT_SWITCH, 0, 0, new Date().getTime()));
//                        Toast.makeText(ScanDeviceActivity.this, mCommonUtil.byteToHexWithSpaceFormat(Arrays.copyOfRange(scanRecord, 11, 17)), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                }
            };
        }else {
            mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi, final byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!mDeviceUtil.isExisted(device.getAddress())) {
                                mAdapter.addDevice(new Device(device.getName(), device.getAddress(), Device.Device_Type.LIGHT_SWITCH, 0, 0, new Date().getTime()));
//                        Toast.makeText(ScanDeviceActivity.this, mCommonUtil.byteToHexWithSpaceFormat(Arrays.copyOfRange(scanRecord, 11, 17)), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            };
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("This app needs location access");
                builder.setMessage("Please grant location access so this app can detect beacons.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @TargetApi(Build.VERSION_CODES.M)
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                    }
                });
                builder.show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "coarse location permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }

                    });
                    builder.show();
                }
                return;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return (true);
        }

        return (super.onOptionsItemSelected(item));
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.
        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }

        // Initializes list view adapter.
        scanLeDevice(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();
        scanLeDevice(false);
        mAdapter.clear();
    }

    private void scanLeDevice(final boolean enable) {
        updateRefreshUI(enable);
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(mStopScanRunable, SCAN_PERIOD);
            mScanning = true;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mBluetoothLeScanner.startScan(mScanFilters, mScanSettings, mScanCallback);
            } else {
                mBluetoothAdapter.startLeScan(mLeScanCallback);
            }
        } else {
            mScanning = false;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mBluetoothLeScanner.flushPendingScanResults(mScanCallback);
                mBluetoothLeScanner.stopScan(mScanCallback);
            } else {
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
            }
            mHandler.removeCallbacks(mStopScanRunable);
        }
    }

    Runnable mStopScanRunable = new Runnable() {
        @Override
        public void run() {
            mScanning = false;
            updateRefreshUI(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mBluetoothLeScanner.flushPendingScanResults(mScanCallback);
                mBluetoothLeScanner.stopScan(mScanCallback);
            } else {
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
            }
        }
    };

    private void updateRefreshUI(boolean enable) {

        if (enable) {
            mFabReload.setShowProgressBackground(true);
            mFabReload.setIndeterminate(true);
            mFabReload.setImageResource(R.drawable.ic_stop_white_24dp);
        } else {
//            mFabReload.setProgress(0, true);
//            mFabReload.hideProgress();
            mFabReload.setShowProgressBackground(false);
//            mFabReload.setIndeterminate(false);
            mFabReload.setImageResource(R.drawable.ic_refresh_white_24dp);
        }
    }

    private DeviceUtil mDeviceUtil = DeviceUtil.getInstance();
    private CommonUtil mCommonUtil = CommonUtil.getInstance();
    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback;

    private ScanCallback mScanCallback;

    public void showInputDialog(final Device device) {
        new MaterialDialog.Builder(this)
                .title(R.string.input_pin_title)
                .content(R.string.input_pin_content)
                .inputType(InputType.TYPE_CLASS_NUMBER |
                        InputType.TYPE_NUMBER_FLAG_SIGNED)
                .inputRange(5, 5)
                .negativeText(android.R.string.cancel)
                .positiveText(android.R.string.ok)
                .input(R.string.input_pin_hint, R.string.input_pin_prefill, false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        Intent intent = new Intent(ScanDeviceActivity.this, DeviceControlActivity.class);
                        intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_NAME, device.getName());
                        intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_ADDRESS, device.getAddress());
                        intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_NEW, true);
                        int pin;
                        try {
                            pin = Integer.valueOf(input.toString());
                        } catch (Exception e) {
                            pin = -1;
                        }
                        intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_PIN, pin);
                        startActivity(intent);
                        finish();
                    }

                }).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

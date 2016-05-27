package com.tmp.smartthings.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.activeandroid.ActiveAndroid;
import com.github.clans.fab.FloatingActionButton;
import com.tmp.smartthings.R;
import com.tmp.smartthings.model.Device;
import com.tmp.smartthings.util.DeviceUtil;
import com.tmp.smartthings.view.adapter.ListDeviceAdapter;

import java.util.List;

public class ListDeviceActivity extends AppCompatActivity {

    private static final String TAG = ListDeviceActivity.class.getName();
    private ListView mListView;
    private FloatingActionButton mFabAdd;
    private ListDeviceAdapter mAdapter;
    private TextView mTitle;
    private DeviceUtil mDeviceUtil = DeviceUtil.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
        ActiveAndroid.initialize(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mTitle = (TextView) toolbar.findViewById(R.id.tv_title);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mListView = (ListView) findViewById(R.id.lv_device_list);
        mAdapter = new ListDeviceAdapter(this);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ListDeviceActivity.this, DeviceControlActivity.class);
                Device device = mAdapter.getItem(position);
                intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_NAME, device.getName());
                intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_ADDRESS, device.getAddress());
                intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_PIN, device.getPin());
                intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_NEW, false);
                startActivity(intent);
            }
        });

        mFabAdd = (FloatingActionButton) findViewById(R.id.fab_device_list_add);
        mFabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListDeviceActivity.this, ScanDeviceActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Device> devices = mDeviceUtil.getAll();
        mAdapter.updateDevices(devices);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_device_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

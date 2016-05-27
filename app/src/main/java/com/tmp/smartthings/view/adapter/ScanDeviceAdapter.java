package com.tmp.smartthings.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tmp.smartthings.R;
import com.tmp.smartthings.model.Device;

import java.util.Collections;
import java.util.List;

/**
 * Created by phapli on 17/05/2016.
 */
public class ScanDeviceAdapter extends BaseAdapter {

    private List<Device> devices = Collections.emptyList();

    private final Context context;

    // the context is needed to inflate views in getView()
    public ScanDeviceAdapter(Context context) {
        this.context = context;
    }

    public void updateDevices(List<Device> devices) {
        this.devices = devices;
        notifyDataSetChanged();
    }


    public void addDevice(Device device) {
        if(device==null || device.getAddress()==null){
            return;
        }
        for(Device ele: this.devices){
            if(ele.getAddress().equals(device.getAddress())){
                return;
            }
        }
        this.devices.add(device);
        notifyDataSetChanged();
    }

    public void clear() {
        devices.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return devices.size();
    }

    // getItem(int) in Adapter returns Object but we can override
    // it to BananaPhone thanks to Java return type covariance
    @Override
    public Device getItem(int position) {
        return devices.get(position);
    }

    // getItemId() is often useless, I think this should be the default
    // implementation in BaseAdapter
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Let's look at that later

        ImageView iv_icon;
        TextView tv_name;
        TextView tv_address;
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_device_list, parent, false);
             iv_icon = (ImageView) convertView.findViewById(R.id.iv_device_list_icon);
             tv_name = (TextView) convertView.findViewById(R.id.tv_device_list_name);
             tv_address = (TextView) convertView.findViewById(R.id.tv_device_list_address);
            convertView.setTag(new ViewHolder(iv_icon, tv_name, tv_address));
        }else {
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            iv_icon = viewHolder.icon;
            tv_name = viewHolder.name;
            tv_address = viewHolder.address;
        }
        Device device = getItem(position);
        tv_name.setText(device.getName());
        tv_address.setText(device.getAddress());
//        iv_icon.setImageResource();

        return convertView;
    }

    private static class ViewHolder {
        public final ImageView icon;
        public final TextView name;
        public final TextView address;

        public ViewHolder(ImageView icon, TextView name, TextView address) {
            this.icon = icon;
            this.name = name;
            this.address = address;
        }
    }

}



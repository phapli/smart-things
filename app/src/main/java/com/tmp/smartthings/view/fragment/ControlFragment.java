package com.tmp.smartthings.view.fragment;

/**
 * Created by phapli on 08/06/2016.
 */

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tmp.smartthings.R;
import com.tmp.smartthings.view.activity.DeviceControlActivity;
import com.tmp.smartthings.view.adapter.SectionsPagerAdapter;

/**
 * SectionsPagerAdapter placeholder fragment containing a simple view.
 */
public class ControlFragment extends Fragment implements SectionFragment{
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String TAG = ControlFragment.class.getName();
    private ImageView mSwitch;
    private ImageView mImageStatus;
    private TextView mTextStatus;

    public ControlFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ControlFragment newInstance() {
        ControlFragment fragment = new ControlFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    ControlListener mCallback;

    // Container Activity must implement this interface
    public interface ControlListener {
        void onDeviceSwitch();
        void onInit(int section);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
    }

    @Override
    public void onResume() {
        super.onResume();
        mCallback.onInit(SectionsPagerAdapter.CONTROL_SECTION);
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (ControlListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ControlListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_control, container, false);
        mSwitch = (ImageView) rootView.findViewById(R.id.iv_device_control_switch);

        mImageStatus = (ImageView) rootView.findViewById(R.id.iv_control_device_status);
        mTextStatus = (TextView) rootView.findViewById(R.id.tv_control_device_status);

        mSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onDeviceSwitch();
            }
        });


        return rootView;
    }

    @Override
    public void updateConnectionStatus(DeviceControlActivity.ConnectionStatus mConnectionStatus) {
        switch (mConnectionStatus) {
            case SEARCHING:
            case CONNECTED:
            case DISCOVERED:
                mImageStatus.setImageResource(R.drawable.ic_bluetooth_searching_white_24dp);
                mImageStatus.setColorFilter(Color.YELLOW);
                mTextStatus.setText(R.string.scanning);
                break;
            case AUTHENTICATED:
                mImageStatus.setImageResource(R.drawable.ic_bluetooth_connected_white_24dp);
                mImageStatus.setColorFilter(Color.GREEN);
                mTextStatus.setText(R.string.available);
                break;
            case DISCONNECTED:
                mImageStatus.setImageResource(R.drawable.ic_bluetooth_disabled_white_24dp);
                mImageStatus.setColorFilter(Color.RED);
                mTextStatus.setText(R.string.unavailable);
                break;
        }

    }

    public void updateSwitchStage(boolean enable) {
        if (enable) {
            mSwitch.setImageResource(R.drawable.light_bulb_green);
        } else {
            mSwitch.setImageResource(R.drawable.light_bulb_gray);
        }
    }
}
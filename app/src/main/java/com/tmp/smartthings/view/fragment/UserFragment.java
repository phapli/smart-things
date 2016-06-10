package com.tmp.smartthings.view.fragment;

/**
 * Created by phapli on 08/06/2016.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.tmp.smartthings.R;
import com.tmp.smartthings.model.Device;
import com.tmp.smartthings.model.User;
import com.tmp.smartthings.util.UserUtil;
import com.tmp.smartthings.view.activity.DeviceControlActivity;
import com.tmp.smartthings.view.adapter.ListDeviceAdapter;

import java.util.List;

public class UserFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private ImageView mImageStatus;
    private TextView mTextStatus;

    private static final String TAG = UserFragment.class.getName();
    private ListView mListView;
    private ListDeviceAdapter mAdapter;
    private List<User> mUsers;

    public UserFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static UserFragment newInstance(int sectionNumber) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    UserListener mCallback;

    // Container Activity must implement this interface
    public interface UserListener {
        void onRemoveUser();
        void onUnbanUser();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (UserListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement ControlListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_user, container, false);

        mImageStatus = (ImageView) rootView.findViewById(R.id.iv_control_device_status);
        mTextStatus = (TextView) rootView.findViewById(R.id.tv_control_device_status);

        mListView = (ListView) rootView.findViewById(R.id.lv_user_list);
        mAdapter = new ListDeviceAdapter(getActivity());
        mListView.setAdapter(mAdapter);


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO
            }
        });

        return rootView;
    }

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
}
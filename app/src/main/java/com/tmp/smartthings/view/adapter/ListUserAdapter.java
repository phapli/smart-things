package com.tmp.smartthings.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tmp.smartthings.R;
import com.tmp.smartthings.model.User;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by phapli on 17/05/2016.
 */
public class ListUserAdapter extends BaseAdapter {

    private List<User> users = Collections.emptyList();

    private SimpleDateFormat sdf = new SimpleDateFormat("dd MMM HH:mm");

    private final Context context;

    // the context is needed to inflate views in getView()
    public ListUserAdapter(Context context) {
        this.context = context;
    }

    public void updateUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }


    public void addUser(User user) {
        if(user==null || user.getAddress()==null){
            return;
        }
        for(User ele: this.users){
            if(ele.getAddress().equals(user.getAddress())){
                return;
            }
        }
        this.users.add(user);
        notifyDataSetChanged();
    }

    public void clear() {
        users.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return users.size();
    }

    // getItem(int) in Adapter returns Object but we can override
    // it to BananaPhone thanks to Java return type covariance
    @Override
    public User getItem(int position) {
        return users.get(position);
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
                    .inflate(R.layout.item_user_list, parent, false);
             iv_icon = (ImageView) convertView.findViewById(R.id.iv_user_list_icon);
             tv_name = (TextView) convertView.findViewById(R.id.tv_user_list_name);
             tv_address = (TextView) convertView.findViewById(R.id.tv_user_list_address);
            convertView.setTag(new ViewHolder(iv_icon, tv_name, tv_address));
        }else {
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            iv_icon = viewHolder.icon;
            tv_name = viewHolder.name;
            tv_address = viewHolder.address;
        }
        User user = getItem(position);
        tv_name.setText(user.getName());
        tv_address.setText(user.getAddress());
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



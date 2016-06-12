package com.tmp.smartthings.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tmp.smartthings.R;
import com.tmp.smartthings.model.ActionLog;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

/**
 * Created by phapli on 17/05/2016.
 */
public class ListLogAdapter extends BaseAdapter {

    private List<ActionLog> actionLogs = Collections.emptyList();

    private SimpleDateFormat sdf = new SimpleDateFormat("dd MMM HH:mm");

    private final Context context;

    // the context is needed to inflate views in getView()
    public ListLogAdapter(Context context) {
        this.context = context;
    }

    public void updateLogs(List<ActionLog> logs) {
        this.actionLogs = logs;
        notifyDataSetChanged();
    }


    public void addLog(ActionLog actionLog) {
        if(actionLog ==null || actionLog.getAddress()==null){
            return;
        }
        this.actionLogs.add(actionLog);
        notifyDataSetChanged();
    }

    public void clear() {
        actionLogs.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return actionLogs.size();
    }

    // getItem(int) in Adapter returns Object but we can override
    // it to BananaPhone thanks to Java return type covariance
    @Override
    public ActionLog getItem(int position) {
        return actionLogs.get(position);
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
        TextView tv_action;
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_log_list, parent, false);
             iv_icon = (ImageView) convertView.findViewById(R.id.iv_log_list_icon);
             tv_name = (TextView) convertView.findViewById(R.id.tv_log_list_name);
             tv_action = (TextView) convertView.findViewById(R.id.tv_log_list_action);
            convertView.setTag(new ViewHolder(iv_icon, tv_name, tv_action));
        }else {
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            iv_icon = viewHolder.icon;
            tv_name = viewHolder.name;
            tv_action = viewHolder.action;
        }
        ActionLog log = getItem(position);
        tv_name.setText(log.getAddress());
        tv_action.setText(log.getAction());
//        iv_icon.setImageResource();

        return convertView;
    }

    private static class ViewHolder {
        public final ImageView icon;
        public final TextView name;
        public final TextView action;

        public ViewHolder(ImageView icon, TextView name, TextView action) {
            this.icon = icon;
            this.name = name;
            this.action = action;
        }
    }

}



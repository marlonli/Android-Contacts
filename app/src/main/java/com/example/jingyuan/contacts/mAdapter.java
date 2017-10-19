package com.example.jingyuan.contacts;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by jingyuan on 10/11/17.
 */

public class mAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    public List<Contact> contacts;

    public mAdapter(Activity activity, List<Contact> contacts) {
        this.activity = activity;
        this.contacts = contacts;
    }

    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public Object getItem(int i) {
        return contacts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Use view holder to set tag for each view
        ViewHolder mHolder = null;

        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_items, null);

            // Get TextView and CheckBOx
            mHolder = new ViewHolder();
            mHolder.tv = (TextView) convertView.findViewById(R.id.contact_name);
            CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
            mHolder.cb = (CheckBox) convertView.findViewById(R.id.checkBox);
            convertView.setTag(mHolder);
        }
        else {
            mHolder = (ViewHolder) convertView.getTag();
        }

        // Set TextView and CheckBox status
        mHolder.tv.setText(contacts.get(position).getName());
        mHolder.cb.setChecked(contacts.get(position).getChecked());

        // Set check box onClick event
        final ViewHolder finalMHolder = mHolder;
        mHolder.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // set checked property
                contacts.get(position).setChecked(finalMHolder.cb.isChecked());

            }
        });

        return convertView;
    }

    final class ViewHolder {
        public TextView tv;
        public CheckBox cb;
    }


}

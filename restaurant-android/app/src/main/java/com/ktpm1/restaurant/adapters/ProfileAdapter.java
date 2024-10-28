package com.ktpm1.restaurant.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ktpm1.restaurant.R;

    public class ProfileAdapter extends BaseAdapter{
        private final Context context;
        private final String[] settingsOptions;
        private final int[] icons;

    public ProfileAdapter(Context context, String[] settingsOptions, int[] icons) {
        this.context = context;
        this.settingsOptions = settingsOptions;
        this.icons = icons;
    }

    @Override
    public int getCount() {
        return settingsOptions.length;
    }

    @Override
    public Object getItem(int position) {
        return settingsOptions[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_profile, parent, false);
        }

        ImageView icon = convertView.findViewById(R.id.item_icon);
        TextView text = convertView.findViewById(R.id.item_text);

        icon.setImageResource(icons[position]);
        text.setText(settingsOptions[position]);

        return convertView;
    }
}

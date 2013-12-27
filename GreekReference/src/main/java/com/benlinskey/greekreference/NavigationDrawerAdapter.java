package com.benlinskey.greekreference;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Based on the tutorial at http://www.michenux.net/android-navigation-drawer-748.html.
 */
public class NavigationDrawerAdapter extends ArrayAdapter<NavigationDrawerItem> {
    private LayoutInflater inflater;

    public NavigationDrawerAdapter(Context context, int id, NavigationDrawerItem[] objects) {
        super(context, id, objects);
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        NavigationDrawerItem item = this.getItem(position);
        if (item.isRow()) {
            view = getRowView(convertView, parent, item);
        } else {
            view = getHeadingView(convertView, parent, item);
        }
        return view;
    }

    public View getRowView(View convertView, ViewGroup parentView, NavigationDrawerItem item) {
        NavigationDrawerRow row = (NavigationDrawerRow) item;
        NavigationDrawerRowHolder navigationDrawerRowHolder = null;

        if (null == convertView) {
            convertView = inflater.inflate(R.layout.navigation_drawer_row, parentView, false);
            TextView textView = (TextView) convertView.findViewById(R.id.navigation_drawer_row_text);
            ImageView imageView = (ImageView)
                    convertView.findViewById(R.id.navigation_drawer_row_icon);

            navigationDrawerRowHolder = new NavigationDrawerRowHolder();
            navigationDrawerRowHolder.textView = textView;
            navigationDrawerRowHolder.imageView = imageView;

            convertView.setTag(navigationDrawerRowHolder);
        }

        if (null == navigationDrawerRowHolder) {
            navigationDrawerRowHolder =(NavigationDrawerRowHolder) convertView.getTag();
        }

        navigationDrawerRowHolder.textView.setText(item.getLabel());
        navigationDrawerRowHolder.imageView
                .setImageResource(((NavigationDrawerRow) item).getIcon());

        return convertView;
    }

    public View getHeadingView(View convertView, ViewGroup parentView, NavigationDrawerItem item) {
        NavigationDrawerHeading heading = (NavigationDrawerHeading) item;
        NavigationDrawerHeadingHolder holder = null;

        if (null == convertView) {
            convertView = inflater.inflate(R.layout.navigation_drawer_heading, parentView, false);
            TextView textView = (TextView)
                    convertView.findViewById(R.id.navigation_drawer_heading_text);

            holder = new NavigationDrawerHeadingHolder();
            holder.textView = textView;
            convertView.setTag(holder);
        }

        if (null == holder) {
            holder = (NavigationDrawerHeadingHolder) convertView.getTag();
        }

        holder.textView.setText(heading.getLabel());

        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return this.getItem(position).getType();
    }

    @Override
    public boolean isEnabled(int position) {
        return getItem(position).isEnabled();
    }

    private static class NavigationDrawerRowHolder {
        private TextView textView;
        private ImageView imageView;
    }

    private static class NavigationDrawerHeadingHolder {
        private TextView textView;
    }



}

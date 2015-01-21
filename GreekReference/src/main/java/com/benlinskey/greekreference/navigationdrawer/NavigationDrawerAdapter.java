/*
 * Copyright 2014 Benjamin Linskey
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.benlinskey.greekreference.navigationdrawer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.benlinskey.greekreference.R;

/**
 * Based on the tutorial at {@llink http://www.michenux.net/android-navigation-drawer-748.html}.
 */
public class NavigationDrawerAdapter extends ArrayAdapter<AbstractNavigationDrawerItem> {

    private final LayoutInflater mInflater;
    private final Context mContext;

    public NavigationDrawerAdapter(Context context, int id, AbstractNavigationDrawerItem[] objects) {
        super(context, id, objects);
        this.mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        AbstractNavigationDrawerItem item = this.getItem(position);
        if (item.isRow()) {
            view = getRowView(convertView, parent, item, position);
        } else {
            view = getHeadingView(convertView, parent, item, position);
        }
        return view;
    }

    public View getRowView(View convertView, ViewGroup parentView, AbstractNavigationDrawerItem item,
            int position) {
        NavigationDrawerRowHolder navigationDrawerRowHolder = null;

        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.navigation_drawer_row, parentView, false);
            TextView textView = (TextView)
                    convertView.findViewById(R.id.navigation_drawer_row_text);
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

        // Text and icon color are determined by whether the item is checked.
        if (((ListView) parentView).isItemChecked(position)) {
            // Item is highlighted.
            ((NavigationDrawerRow) item).setIconHighlighted(true);
            TextView textView = navigationDrawerRowHolder.textView;
            int textDark = mContext.getResources().getColor(R.color.primary_dark_material_dark);
            int backgroundLight =
                    mContext.getResources().getColor(R.color.background_material_light);
            textView.setTextColor(textDark);
            convertView.setBackgroundColor(backgroundLight);
        } else {
            // Item is not highlighted.
            ((NavigationDrawerRow) item).setIconHighlighted(false);
            TextView textView = navigationDrawerRowHolder.textView;
            int textDark = mContext.getResources().getColor(R.color.primary_dark_material_dark);
            int backgroundWhite = mContext.getResources().getColor(R.color.background_white);
            textView.setTextColor(textDark);
            convertView.setBackgroundColor(backgroundWhite);
        }

        navigationDrawerRowHolder.imageView
                .setImageResource(((NavigationDrawerRow) item).getIcon());

        return convertView;
    }

    public View getHeadingView(View convertView, ViewGroup parentView, AbstractNavigationDrawerItem item,
            int position) {
        NavigationDrawerHeadingHolder holder = null;
        TextView textView;

        if (null == convertView) {
            if (0 == position) {
                convertView = mInflater.inflate(R.layout.navigation_drawer_heading_first,
                        parentView, false);
            } else {
                convertView = mInflater.inflate(R.layout.navigation_drawer_heading,
                        parentView, false);
            }
            textView = (TextView) convertView.findViewById(R.id.navigation_drawer_heading_text);

            holder = new NavigationDrawerHeadingHolder();
            holder.textView = textView;
            convertView.setTag(holder);
        }

        if (null == holder) {
            holder = (NavigationDrawerHeadingHolder) convertView.getTag();
        }

        holder.textView.setText(item.getLabel());

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

package com.benlinskey.greekreference;

import android.content.Context;
import android.util.Log;
import android.widget.Checkable;

/**
 * Based on the tutorial at http://www.michenux.net/android-navigation-drawer-748.html.
 */
public class NavigationDrawerRow extends NavigationDrawerItem {

    private static final int TYPE = 1;
    private int mIcon;

    public NavigationDrawerRow(int id, String label, String icon, Context context) {
        super(id, label);
        mIcon = context.getResources().getIdentifier(icon, "drawable", context.getPackageName());
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isRow() {
        return true;
    }

    @Override
    public boolean updateActionBarTitle() {
        return false;
    }

    public int getIcon() {
        return mIcon;
    }

    public void setIcon(int icon) {
        mIcon = icon;
    }

    @Override
    public int getType() {
        return TYPE;
    }
}

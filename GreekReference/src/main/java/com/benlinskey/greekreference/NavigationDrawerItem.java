package com.benlinskey.greekreference;

/**
 * Based on the tutorial at http://www.michenux.net/android-navigation-drawer-748.html.
 */
public abstract class NavigationDrawerItem {

    private int mId;
    private String mLabel;

    public NavigationDrawerItem(int id, String label) {
        mId = id;
        mLabel = label;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getLabel() {
        return mLabel;
    }

    public void setLabel(String label) {
        mLabel = label;
    }

    public abstract boolean isEnabled();
    public abstract boolean isRow();
    public abstract int getType();
    public abstract boolean updateActionBarTitle();
}

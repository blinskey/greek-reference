package com.benlinskey.greekreference;

/**
 * Based on the tutorial at http://www.michenux.net/android-navigation-drawer-748.html.
 */
public class NavigationDrawerHeading extends NavigationDrawerItem {

    private static final int TYPE = 0;

    public NavigationDrawerHeading(int id, String label) {
        super(id, label);
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public boolean isRow() {
        return false;
    }

    @Override
    public boolean updateActionBarTitle() {
        return false;
    }

    @Override
    public int getType() {
        return TYPE;
    }
}

package com.benlinskey.greekreference;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Checkable;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * This is a custom LinearLayout that allows items to be selected and highlighted.
 * <p>
 * This class is based on advice gleaned from the following sources:
 * <ul>
 *     <li>http://tokudu.com/post/50023900640/android-checkable-linear-layout</li>
 *     <li>http://stackoverflow.com/questions/8369640/listview-setitemchecked-only-works-with-standard-arrayadapter-does-not-work-w</li>
 * </ul>
 */
public class CheckableLinearLayout extends LinearLayout implements Checkable {
    private boolean mIsChecked = false;

    public CheckableLinearLayout(Context context) {
        super(context);
    }

    public CheckableLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckableLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setChecked(boolean b) {
        mIsChecked = b;
        updateColor();
    }

    @Override
    public boolean isChecked() {
        return mIsChecked;
    }

    @Override
    public void toggle() {
        if (mIsChecked) {
            mIsChecked = false;
        } else {
            mIsChecked = true;
        }
        updateColor();
    }

    /**
     * Updates the background, text, and icon colors to reflect the current checked or unchecked
     * state of this row.
     */
    private void updateColor() {
        // TODO: Switch icon color once icons are implemented.
        if (mIsChecked) {
            setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
            ((TextView) findViewById(R.id.navigation_drawer_row_text))
                    .setTextColor(getResources().getColor(android.R.color.white));
        } else {
            setBackgroundColor(getResources().getColor(android.R.color.transparent));
            ((TextView) findViewById(R.id.navigation_drawer_row_text))
                    .setTextColor(getResources().getColor(android.R.color.black));
        }
    }
}

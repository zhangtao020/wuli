package com.wuliwuli.haitao.listener;

import android.support.v4.view.ViewPager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * Created by mac-z on 16/4/17.
 */
public class ViewPagerIndicator implements ViewPager.OnPageChangeListener {

    RadioGroup index_radiogroup ;

    public ViewPagerIndicator(RadioGroup group){
        this.index_radiogroup = group;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
//        lastPosition = current_index;
//        updateHandler.removeMessages(lastPosition);
//        current_index = position;
//        AppApplication.CURRENT_INDEX = lastPosition;

        int childCount = index_radiogroup.getChildCount();
        if (childCount > 0) {
            RadioButton childAt = (RadioButton) index_radiogroup.getChildAt(position);
            if (childAt != null) {
                childAt.setChecked(true);
            }
        }


    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

}

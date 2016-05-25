package com.wuliwuli.haitao;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.wuliwuli.haitao.base.AppBaseActivity;
import com.wuliwuli.haitao.util.ScreenUtil;
import com.wuliwuli.haitao.util.WuliConfig;

import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends AppBaseActivity {

    /**
     * 页面滑动
     */
    private ViewPager viewPager;
    /**
     * 适配器
     */
    private ViewPagerAdater adater;
    /**
     * 图片数据源
     */
    private List<View> list;

    private RadioGroup radioGroup;
    private RadioButton[] radioButtons;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initView();

    }

    private int  flaggingWidth;

    private void initView() {
        gestureDetector = new GestureDetector(new GuideViewTouch());
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        list = new ArrayList<>();
        setData();
        adater = new ViewPagerAdater();
        viewPager.setAdapter(adater);

        flaggingWidth = ScreenUtil.getScreenWidth(this) / 4;
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                viewPager.setCurrentItem(position);
                RadioButton rb = radioButtons[position];
                rb.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        int count = radioGroup.getChildCount();
        radioButtons = new RadioButton[count];
        for (int k = 0; k < count; k++) {
            radioButtons[k] = (RadioButton) radioGroup.getChildAt(k);
        }

        RadioButton rb = radioButtons[0];
        rb.setChecked(true);


    }

    /**
     * 添加数据
     */
    private void setData() {

        ImageView imageView1 = new ImageView(this);
        imageView1.setImageResource(R.drawable.welcome1);

        ImageView imageView2 = new ImageView(this);
        imageView2.setImageResource(R.drawable.welcome2);

        ImageView imageView3 = new ImageView(this);
        imageView3.setImageResource(R.drawable.welcome3);

        ImageView imageView4 = new ImageView(this);
        imageView4.setImageResource(R.drawable.welcome4);

        list.add(imageView1);
        list.add(imageView2);
        list.add(imageView3);
        list.add(imageView4);
    }


    public void start() {
        Intent intent = new Intent(this, LoginActivity_.class);
        startActivity(intent);
        finish();
    }

    /**
     * 适配器
     * 当 ViewPager 和 Fragment 一起使用时,适配器应该为 FragmentPagerAdapter,实现 getCount 和 getItem 方法
     * 当 ViewPager 和 其他的使用时,适配器为 PagerAdapter ,实现 instantiateItem destoryItem getCount isViewFromObject
     */
    private class ViewPagerAdater extends PagerAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
//            return super.instantiateItem(container, position);
            ((ViewPager) container).addView(list.get(position));
            return list.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);
            ((ViewPager) container).removeView(list.get(position));
        }
    }

    private void checkIsLogin() {
        boolean is_login = WuliConfig.getInstance().getBooleanInfoFromLocal(WuliConfig.IS_LOGIN, false);
        if (is_login) {
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {

        }
    }

    private GestureDetector gestureDetector; // 用户滑动

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event)) {
            event.setAction(MotionEvent.ACTION_CANCEL);
        }
        return super.dispatchTouchEvent(event);
    }

    private class GuideViewTouch extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (viewPager.getCurrentItem() == 3) {
                if (Math.abs(e1.getX() - e2.getX()) > Math.abs(e1.getY() - e2.getY()) && (e1.getX() - e2.getX() <= (-flaggingWidth) || e1.getX() - e2.getX() >= flaggingWidth)) {
                    if (e1.getX() - e2.getX() >= flaggingWidth) {
                        start();
                        return true;
                    }
                }
            }
            return false;
        }
    }

}

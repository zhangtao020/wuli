package com.wuliwuli.haitao.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.wuliwuli.haitao.base.AppApplication;
import com.wuliwuli.haitao.transform.Transformation;

import java.util.List;

/**
 * Created by mac-z on 16/4/16.
 */
public class ProductImgAdapter extends PagerAdapter {

    List<ImageView> list;
    Context mContext;
    Picasso picasso = null;
    int mScreenWidth;
    int mShowHeight;
    ViewPager mViewPager;

    public ProductImgAdapter(Context context,List<ImageView> list,ViewPager viewPager){
        this.mContext = context;
        this.list = list;
        this.mViewPager = viewPager;
        mScreenWidth = AppApplication.DISPLAY_WIDTH;
        picasso = Picasso.with(mContext);
    }

    public int getPictureHeight(){
        return mShowHeight;
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(mShowHeight != 0)return;
            mShowHeight = (int) msg.obj;
            FrameLayout.LayoutParams pagerParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,mShowHeight);
            mViewPager.setLayoutParams(pagerParams);
        }
    };

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
        ImageView imageView = list.get(position);
        ((ViewPager) container).addView(imageView);
        String imgUrl = (String)imageView.getTag();
        picasso.load(imgUrl).transform(new Transformation(mScreenWidth,mHandler)).into(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (position < list.size()) {
            ((ViewPager) container).removeView(list.get(position));
        }

    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}

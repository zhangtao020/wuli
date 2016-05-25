package com.wuliwuli.haitao.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.wuliwuli.haitao.adapter.LinearLayoutBaseAdapter;


/**
 * Created by mac-z on 15/8/19.
 */
public class LinearLayoutForListView extends LinearLayout {

    private LinearLayoutBaseAdapter adapter;
    private OnItemClickListener onItemClickListener;

    public LinearLayoutForListView(Context context){
        super(context);
    }

    public LinearLayoutForListView(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    public void setAdapter(LinearLayoutBaseAdapter adapter){
        this.adapter = adapter;

        bindView();
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.onItemClickListener = listener;
    }

    private void bindView(){
        if(adapter == null){
            return;
        }
        for(int i=0;i<adapter.getCount();i++){
            final View v = adapter.getView(i);
            final int temp = i;
            final Object obj = adapter.getItem(i);

            v.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View view) {
                    if(onItemClickListener != null){
                        onItemClickListener.onItemClicked(v,obj,temp);
                    }
                }
            });
            addView(v);
        }
    }

    public void addHeadView(View headView){
        addView(headView);
    }

    public interface OnItemClickListener{

        public void onItemClicked(View v, Object o, int position);
    }

}

package com.wuliwuli.haitao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import java.util.List;

/**
 * Created by mac-z on 15/8/19.
 */
public abstract class LinearLayoutBaseAdapter<T> {

    public List<T> list;
    public Context context;

    public LinearLayoutBaseAdapter(Context context, List<T> list){
        this.context = context;
        this.list = list;
    }

    public LayoutInflater getLayoutInflater(){
        if(context != null){
            return LayoutInflater.from(context);
        }
        return null;
    }

    public int getCount(){
        if(list != null){
            return  list.size();
        }
        return 0;
    }

    public Object getItem(int position){
        if(list == null){
            return null;
        }
        return  list.get(position);
    }


   public abstract View getView(int position);
}

package com.wuliwuli.haitao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.wuliwuli.haitao.R;
import com.wuliwuli.haitao.bean.CommentBean;

import java.util.List;

/**
 * Created by mac-z on 16/4/15.
 */
public class TopicCommentAdapter extends LinearLayoutBaseAdapter{


    public TopicCommentAdapter(Context context, List list) {
        super(context, list);
    }

    @Override
    public View getView(int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_topic_comment,null);
        CommentBean bean = (CommentBean) list.get(position);
        ((TextView)view.findViewById(R.id.item_topic_ct_name_tv)).setText(bean.nick_name+"：");
        ((TextView)view.findViewById(R.id.item_topic_ct_tv)).setText(bean.content);
        return view;
    }
}

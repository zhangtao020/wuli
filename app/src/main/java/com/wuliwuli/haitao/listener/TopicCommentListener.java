package com.wuliwuli.haitao.listener;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;

import com.wuliwuli.haitao.R;
import com.wuliwuli.haitao.adapter.TopicCommentAdapter;
import com.wuliwuli.haitao.bean.ProductBean;
import com.wuliwuli.haitao.view.LinearLayoutForListView;

/**
 * Created by mac-z on 16/4/15.
 */
public class TopicCommentListener implements View.OnClickListener{

    Context mContext;
    ImageButton showMoreIb;
    LinearLayoutForListView commentLv;
    ProductBean dataBean;

    public TopicCommentListener(Context context,ImageButton ib,LinearLayoutForListView lv,ProductBean dataBean){
        this.mContext = context;
        this.showMoreIb = ib;
        this.commentLv = lv;
        this.dataBean = dataBean;
    }

    @Override
    public void onClick(View v) {
        String keyT = (String)showMoreIb.getTag(R.string.key_tag);
        if(!dataBean.product_id.equals(keyT))return;
        String tag = (String) showMoreIb.getTag();
        showMoreIb.setTag("up".equals(tag)?"down":"up");
        commentLv.removeAllViews();
        if("up".equals(tag)){
            dataBean.isExpand = false;
            showMoreIb.setImageResource(R.drawable.icon_comment_down);
            commentLv.setAdapter(new TopicCommentAdapter(mContext, dataBean.comment.subList(0,3)));
        }else{
            dataBean.isExpand = true;
            showMoreIb.setImageResource(R.drawable.icon_comment_up);
            commentLv.setAdapter(new TopicCommentAdapter(mContext, dataBean.comment));
        }
    }
}

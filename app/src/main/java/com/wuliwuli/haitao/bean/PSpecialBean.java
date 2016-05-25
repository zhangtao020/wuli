package com.wuliwuli.haitao.bean;

import android.text.style.TtsSpan;
import android.widget.BaseAdapter;

import java.util.Date;
import java.util.List;

/**
 * Created by mac-z on 16/4/13.
 */
public class PSpecialBean extends BaseResult {

    public ContentBean data;

    public static class ContentBean {
        public String link;
        public String icon;
        public SpecBean special;
    }

    public static class SpecBean extends DataBean{
        public String created;
        public String liked_sum;
        public List<TopicBean> topic;
    }

    public static class TopicBean{
        public String id;
        public String cover_img;
        public List<ProductBean> product;
        public String liked_sum;
    }
}

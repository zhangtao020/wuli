package com.wuliwuli.haitao.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mac-z on 16/4/13.
 */
public class PTopicBean extends BaseResult {

    public ContentBean data;

    public static class ContentBean {
        public String link;
        public String icon;
        public SpecBean topic;
    }

    public static class SpecBean extends DataBean{
        public String created;
        public String liked_sum;
        public List<ProductBean> product;
        public List<FaceBean> user_liked;
    }

    public static class FaceBean implements Serializable{
        public String face;
    }

}

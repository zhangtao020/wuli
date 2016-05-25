package com.wuliwuli.haitao.bean;

import java.util.List;

/**
 * Created by mac-z on 16/5/22.
 */
public class PSuperBean extends BaseResult {
    public ContentBean data;

    public static class ContentBean {
        public String link;
        public String icon;
        public SuperBean supermoney;
    }

    public static class SuperBean extends DataBean{
        public String created;
        public String supermoney;
        public List<ProductBean> product;
//        public List<FaceBean> user_liked;
    }
}

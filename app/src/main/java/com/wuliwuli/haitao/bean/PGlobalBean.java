package com.wuliwuli.haitao.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/6/2.
 */
public class PGlobalBean extends BaseResult {

    public List<GlobalBean> data ;
    public static class GlobalBean{
        public String id;
        public int type;
        public String cover_img;
        public String title;
        public String description;
        public String disorder;
        public String price_title;
        public String created;
        public String uname;
        public String face;
        public String category;
        public String business;
    }
}

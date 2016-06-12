package com.wuliwuli.haitao.bean;

import java.util.List;

/**
 * Created by mac-z on 16/4/16.
 */
public class PGlobalInfoBean extends BaseResult {

    public ContentBean data;

    public static class ContentBean{
        public GlobalBean floor;
        public String link;
        public String icon;
    }

    public static class GlobalBean{
        public String id;
        public String business_id;
        public String title;
        public String cover_img;
        public String description;
        public String price_title;
        public String country_id;
        public String brand_id;
        public String price;
        public String origin_price;
        public String jd_price;
        public String weipin_price;
        public String kaola_price;
        public int liked_total;
        public String created;
        public String impression;
        public int is_liked;
        public List<PBuyUrlBean.BuyUrlBean> img;
        public String uname;
        public String face;
        public String category;
        public String country_logo;
        public String country_name;
        public String business;
        public String brand_name;
    }
}

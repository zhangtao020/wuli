package com.wuliwuli.haitao.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mac-z on 16/4/13.
 */
public class ProductBean implements Serializable{

/***************详情参数*****************/
    public String id;
    public String admin_id;
    public String business_id;
    public String name;
    public String cover_img;
    public String description;
    public List<String> content;
    public String top_cid;
    public String country_id;
    public String brand_id;
    public String price;
    public String origin_price;
    public String jd_price;
    public String weipin_price;
    public String kaola_price;
    public int is_bonus;
    public int liked_total;
    public String comment_total;
    public String created;
    public int is_liked;
    public List<PBuyUrlBean.BuyUrlBean> img;
    public String uname;
    public String face;
    public String category;
    public String country_logo;
    public String country_name;
    public String business;
    public String brand_name;
/***************end*****************/


    public String discount_price;
    public String product_id;
    public int count_comment;
    public List<CommentBean> comment;
    public boolean isExpand = false;
}

package com.wuliwuli.haitao.bean;

import java.util.List;

/**
 * Created by mac-z on 16/4/19.
 */
public class POrderBean extends BaseResult{

    public List<ContentBean> data;

    public static class ContentBean{
        public String order_time;
        public String user_id;
        public String order_sn;
        public String product_id;
        public int status;
        public String order_amount;
        public String cover_img;
        public String business;
    }
}

package com.wuliwuli.haitao.bean;

/**
 * Created by mac-z on 16/4/18.
 */
public class PPersonBean extends BaseResult{

    public ContentBean data;

    public static class ContentBean{
        public String id;
        public String open_id;
        public String nick_name;
        public String face;
        public String sum_bonus;
    }
}

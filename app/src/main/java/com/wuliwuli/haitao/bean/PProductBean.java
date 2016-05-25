package com.wuliwuli.haitao.bean;

/**
 * Created by mac-z on 16/4/16.
 */
public class PProductBean extends BaseResult {

    public ContentBean data;

    public static class ContentBean{
        public ProductBean product;
        public String link;
        public String icon;
    }
}

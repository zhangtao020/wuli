package com.wuliwuli.haitao.bean;

/**
 * Created by mac-z on 16/5/16.
 */
public class AppInfoBean extends BaseResult {

    public ContentBean data;

    public static class ContentBean{
        public int status;
        public String url;
        public String content = "";
    }
}

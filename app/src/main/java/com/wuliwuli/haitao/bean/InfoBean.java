package com.wuliwuli.haitao.bean;

import java.util.List;

/**
 * Created by mac-z on 16/4/13.
 */
public class InfoBean extends BaseResult{

    public List<ContentBean> data;

    public static class ContentBean extends DataBean{
        public String liked_total;
        public List<ProductBean> product;

    }
}

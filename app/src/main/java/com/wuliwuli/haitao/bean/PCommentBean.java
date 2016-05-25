package com.wuliwuli.haitao.bean;

import java.util.List;

/**
 * Created by mac-z on 16/4/19.
 */
public class PCommentBean extends BaseResult{

    public List<ContentBean> data;

    public static class ContentBean extends CommentBean{
        public String id;
        public String user_id;
        public String reply_id;
        public String product_id;
        public String product_name;
        public String created;
        public String cover_img;
        public String category;
        public String face;
    }
}

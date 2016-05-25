package com.wuliwuli.haitao.bean;

import java.io.Serializable;

/**
 * Created by mac-z on 16/5/8.
 */
public class PWriteCommentBean extends BaseResult {

    public ConntenBean data;

    public static class ConntenBean{
        public Comment comment;
    }

    public static class Comment{
        public String content;
        public String created;
        public String nick_name;
    }

}

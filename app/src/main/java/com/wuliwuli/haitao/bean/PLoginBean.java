package com.wuliwuli.haitao.bean;

/**
 * Created by mac-z on 16/4/25.
 */
public class PLoginBean extends BaseResult {

    public  LoginBean data;

    public static class LoginBean{
        public String id;
        public String user_name;
        public String nick_name;
        public String face;
        public String open_id;
        public String token;
    }
}

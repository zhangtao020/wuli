package com.wuliwuli.haitao.bean;

/**
 * Created by mac-z on 16/4/25.
 */
public class PSmsBean extends BaseResult {

    public SmsBean data;

    public static class SmsBean{
        public String sms;
        public int type;
    }
}

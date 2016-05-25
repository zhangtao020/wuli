package com.wuliwuli.haitao.bean;

import java.util.List;

/**
 * Created by mac-z on 16/4/27.
 */
public class PRecordBean extends BaseResult {

    public ContentBean data;

    public static class ContentBean{
        public int sum;
        public List<RecordBean> record;
    }

    public static class RecordBean{
        public String user_id;
        public String user_name;
        public String user_type;
        public String amount;
        public String created;
    }
}

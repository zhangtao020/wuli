package com.wuliwuli.haitao.bean;

import java.util.List;

/**
 * Created by mac-z on 16/4/21.
 */
public class PMyBonusBean extends BaseResult {

    public ContentBean data;

    public static class ContentBean{
        public CountBean count;
        public List<BonusBean> bonus;
    }

    public static class CountBean{
        public String dj;
        public String kt;
    }

    public static class BonusBean extends POrderBean.ContentBean{
        public String id;
        public String nick_name;
        public String bonus_user;
        public int is_open;
        public String op_id;
    }
}

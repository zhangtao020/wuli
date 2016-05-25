package com.wuliwuli.haitao.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by mac_z on 16/4/25.
 */
public class WuliConfig {

    public static Context context;
    public static WuliConfig instance;

    public static WuliConfig getInstance() {
        if (instance == null) {
            synchronized (WuliConfig.class) {
                if (instance == null) {
                    instance = new WuliConfig();
                }
            }

        }
        return instance;
    }

    /**
     * 保存 String 变量到本地
     * @param key   MiaocheConfig.静态字段
     * @param value 要保存的值
     */
    public void saveStringInfoToLocal(String key, String value) {
        SharedPreferences preferences = context.getSharedPreferences(fileName, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 从本地获取 String 变量
     * @param key       MiaocheConfig.静态字段
     * @param defalut   默认值
     * @return
     */
    public String getStringInfoFromLocal(String key, String defalut) {
        SharedPreferences preferences = context.getSharedPreferences(fileName, context.MODE_PRIVATE);
        String value = preferences.getString(key, defalut);
        return value;
    }

    /**
     * 保存 boolean 变量到本地
     * @param key       MiaocheConfig.静态字段
     * @param value     要保存的值
     * @return
     */
    public void saveBooleanInfoToLocal(String key, boolean value) {
        SharedPreferences preferences = context.getSharedPreferences(fileName, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * 从本地获取 boolean 变量
     * @param key       MiaocheConfig.静态字段
     * @param defalut   默认值
     * @return
     */
    public boolean getBooleanInfoFromLocal(String key, boolean defalut) {
        SharedPreferences preferences = context.getSharedPreferences(fileName, context.MODE_PRIVATE);
        boolean value = preferences.getBoolean(key, defalut);
        return value;
    }

    public static String PHONE_NUMBER = "phonenumber";
    public static String IS_FIRST_USE = "isfirstuse";
    public static String IS_LOGIN = "islogin";
    public static String USER_ID = "user_id";
    public static String OPEN_ID = "open_id";
    public static String TOKEN_ID = "token_id";
    public static String USER_NAME = "user_name";
    public static String USER_FACE = "face";
    public static String NICK_NAME = "nick_name";
    public static String JPUSH_ID = "jpush_id";

    public static String fileName = "wuliwuli";


}

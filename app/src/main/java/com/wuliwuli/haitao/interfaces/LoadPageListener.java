package com.wuliwuli.haitao.interfaces;

/**
 * Created by mac-z on 16/3/5.
 */
public interface LoadPageListener {
    void doLoadSuccess(String msg,String state);
    void doLoadSuccess();
    void doLoadFail();
}

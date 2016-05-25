package com.wuliwuli.haitao.update;

/**
 * 软件更新监听类-目前只在2CAPP中使用
 * Created by hpzhang on 16/3/25.
 */
public interface UpdateListener {

    /**
     * 更新时,确定按钮点击回调
     * @param is_upgrade 1:强制升级  0:非强制升级
     */
    void onSureClickForUpdate(int is_upgrade);

    /**
     * 是否有更新的回调
     */
    void onIsHaveNewVersion(boolean isHaveNewVersion);

    void cancelUpdate();


}

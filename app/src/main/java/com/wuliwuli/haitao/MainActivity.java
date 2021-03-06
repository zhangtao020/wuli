package com.wuliwuli.haitao;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wuliwuli.haitao.base.AppBaseActivity;
import com.wuliwuli.haitao.bean.BaseResult;
import com.wuliwuli.haitao.fragment.GlobalFragment;
import com.wuliwuli.haitao.fragment.HomeFragment;
import com.wuliwuli.haitao.fragment.RedFragment_;
import com.wuliwuli.haitao.http.NormalPostRequest;
import com.wuliwuli.haitao.http.UrlManager;
import com.wuliwuli.haitao.update.CheekAppVersion;
import com.wuliwuli.haitao.update.UpdateListener;
import com.wuliwuli.haitao.util.AppUtil;
import com.wuliwuli.haitao.util.ToastUtil;
import com.wuliwuli.haitao.util.WuliConfig;

import java.util.HashMap;


public class MainActivity extends AppBaseActivity implements UpdateListener {
    TabHost mTabhost;
    Button mGlobalBtn;
    TabManager mTabManager;
    RadioGroup mRadioG;
    RadioButton mRadios[];
    int lastCheckedPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSystemBarTit();

        findview(savedInstanceState);
        initNavigationView();

        // 检查更新
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("device", "Android");
        map.put("version", AppUtil.getVersionCode()+"");
        CheekAppVersion check = CheekAppVersion.showNewAppVersion(this, UrlManager.CHECK_UPDATE, map, R.drawable.ic_launcher);
        check.setUpdateListener(this);

        sendJpushIdToService();
    }

    private void sendJpushIdToService(){
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("client_id", WuliConfig.getInstance().getStringInfoFromLocal(WuliConfig.JPUSH_ID,""));
        map.put("token", WuliConfig.getInstance().getStringInfoFromLocal(WuliConfig.TOKEN_ID,""));

        NormalPostRequest request = new NormalPostRequest(UrlManager.JPUSH_RECEIVE,
                new Response.Listener<BaseResult>() {
                    @Override
                    public void onResponse(BaseResult response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("", error.getMessage(), error);
            }
        }, map, BaseResult.class);
        request.doRequest();
    }



    private void findview(Bundle savedInstanceState){
        mGlobalBtn = (Button) findViewById(R.id.tab_global_btn);
        mTabhost = (TabHost) findViewById(android.R.id.tabhost);
        mTabhost.setup();

        mTabManager = new TabManager(this, mTabhost, android.R.id.tabcontent);
        mGlobalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastCheckedPosition = 1;
                mTabhost.setCurrentTabByTag("b");
                RadioButton rb = (RadioButton) mRadioG.getChildAt(1);
                rb.setChecked(true);
                mGlobalBtn.setTextColor(getResources().getColor(R.color.color_282828));
            }
        });

        mTabManager.addTab(mTabhost.newTabSpec("a").setIndicator("a"), HomeFragment.class, null);
        mTabManager.addTab(mTabhost.newTabSpec("b").setIndicator("b"), GlobalFragment.class, null);
        mTabManager.addTab(mTabhost.newTabSpec("c").setIndicator("c"), RedFragment_.class, null);

        if(savedInstanceState != null){
            mTabhost.setCurrentTabByTag(savedInstanceState.getString("tag"));
        }else{
            mTabhost.setCurrentTabByTag("b");
            mGlobalBtn.setSelected(true);
            mGlobalBtn.setTextColor(getResources().getColor(R.color.color_282828));
        }
    }

    private void initNavigationView(){
        mRadioG = (RadioGroup) findViewById(R.id.btm_navigation);
        mRadios = new RadioButton[3];
        for(int i=0;i<mRadioG.getChildCount();i++){
            RadioButton rb = (RadioButton) mRadioG.getChildAt(i);
            mRadios[i] = rb;
        }

        mRadioG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup rg, int checkedId) {
                for(int i=0;i<rg.getChildCount();i++){
                    RadioButton rb = (RadioButton) rg.getChildAt(i);
                    if(rb.isChecked()) {
                        if(i == 2 && !WuliConfig.getInstance().getBooleanInfoFromLocal(WuliConfig.IS_LOGIN,false)){

                            mTabhost.setCurrentTab(lastCheckedPosition);
                            mRadios[lastCheckedPosition].setChecked(true);
                            if(lastCheckedPosition == 1){
                                mGlobalBtn.setTextColor(getResources().getColor(R.color.color_282828));
                            }
                            Intent intent = new Intent(MainActivity.this,LoginActivity_.class);
                            intent.putExtra("showback",true);
                            startActivity(intent);
                            break;
                        }
                        mGlobalBtn.setTextColor(getResources().getColor(R.color.gray_8c8c8c));
                        mTabhost.setCurrentTab(i);
                        lastCheckedPosition = i;
                        break;
                    }
                }
            }
        });
    }


    @Override
    public void onSureClickForUpdate(int is_upgrade) {
        if (is_upgrade == 0) {
            showLoadingDialog(true, "正在更新...");
        } else if (is_upgrade == 1){
            showLoadingDialog(false, "正在更新...");
        } else {
            showLoadingDialog(true, "正在更新...");
        }
    }

    @Override
    public void onIsHaveNewVersion(boolean isHaveNewVersion) {

    }

    @Override
    public void cancelUpdate() {
        finishAllActivity();
    }

    public class TabManager implements TabHost.OnTabChangeListener {

        FragmentActivity mActivity;
        TabHost mTabhost;
        int mContainerId;
        HashMap<String,TabInfo> mTabs = new HashMap<String,TabInfo>();
        TabInfo mLastTab;
        boolean isMoreTab = false;

        class TabInfo{
            private String tag;
            private Class<?> clss;
            private Bundle args;
            private Fragment fragment;

            TabInfo(String _tag,Class<?> _clss,Bundle _args){
                tag = _tag;
                clss = _clss;
                args = _args;
            }
        }

        class DumyTabFactory implements TabHost.TabContentFactory{

            private Context mContext;

            public DumyTabFactory(Context ctx){
                mContext = ctx;
            }

            @Override
            public View createTabContent(String tag) {
                View v = new View(mContext);
                v.setMinimumWidth(0);
                v.setMinimumHeight(0);

                return v;
            }
        }

        public TabManager(FragmentActivity activity,TabHost tabHost,int containerId){
            mActivity = activity;
            mTabhost = tabHost;
            mContainerId = containerId;
            mTabhost.setOnTabChangedListener(this);
        }

        public void addTab(TabHost.TabSpec tabSpec,Class<?> clss,Bundle args){
            tabSpec.setContent(new DumyTabFactory(mActivity));
            String tag = tabSpec.getTag();

            TabInfo info = new TabInfo(tag, clss, args);
            info.fragment = mActivity.getSupportFragmentManager().findFragmentByTag(tag);
            if(info.fragment !=null && !info.fragment.isDetached()){
                FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
                ft.detach(info.fragment);
                ft.commitAllowingStateLoss();
            }

            mTabs.put(tag, info);
            mTabhost.addTab(tabSpec);
        }

        @Override
        public void onTabChanged(String tabId) {
            TabInfo newTab = mTabs.get(tabId);
            if(mLastTab != newTab){
                FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
                if(mLastTab != null){
                    if(mLastTab.fragment != null){
//                        ft.detach(mLastTab.fragment);
                        ft.hide(mLastTab.fragment);//使用该方法可以保存fragment的状态
                    }
                }
                if(newTab != null){
                    if(newTab.fragment == null){
                        newTab.fragment = Fragment.instantiate(mActivity, newTab.clss.getName(),newTab.args);
                        ft.add(mContainerId, newTab.fragment,newTab.tag);
                    }else{
//                        ft.attach(newTab.fragment);
                        if(newTab.fragment instanceof RedFragment_){
                            ((RedFragment_)newTab.fragment).onResume();
                        }
                        ft.show(newTab.fragment);//使用该方法可以保存fragment的状态
                    }
                }

                mLastTab = newTab;
                ft.commitAllowingStateLoss();
                mActivity.getSupportFragmentManager().executePendingTransactions();
            }
            isMoreTab = "d".equals(tabId);
//			setMessageStatus(MessageCenter.updataNum());
//			setSysStatusBar();
        }

    }

    private long lastBack;
    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis() - lastBack < 800){
            super.onBackPressed();
            return;
        }
        lastBack = System.currentTimeMillis();
        ToastUtil.show("再按一次退出");
    }
}

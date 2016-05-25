package com.wuliwuli.haitao.http;

public class UrlManager {
	private static String MAIN_URL = "http://api.wuliwuli.top/wap.php/";
//	private static String MAIN_URL = "http://sptest.lvniao.com.cn/wap.php/";

	public static String HOME_INDEX = MAIN_URL + "Api/index"; //首页
	public static String SPECIAL_LIST = MAIN_URL + "Api/special";//专题
	public static String TOPIC_LIST = MAIN_URL + "Api/topic";//主题
	public static String PRODUCT_INFO = MAIN_URL + "Api/product_info";//商品详情
	public static String USER_LIKE = MAIN_URL + "Api/user_liked";//收藏
	public static String USER_COMMENT = MAIN_URL + "Api/user_comment";//发布评论
	public static String SCP = MAIN_URL + "Api/scp";//商品跳转
	public static String MY_INDEX = MAIN_URL + "Api/my_index";//个人中心
	public static String MY_LIKE = MAIN_URL + "Api/my_liked";//我的收藏
	public static String MY_ORDER = MAIN_URL + "Api/my_order";//我的订单
	public static String MY_COMMEND = MAIN_URL + "Api/my_commend";//我的评论
	public static String MY_BONUS = MAIN_URL + "Api/my_bonus";//我的红包
	public static String MY_RECORD = MAIN_URL + "Api/my_record";//我的体现

	public static String SMS_LOGIN = MAIN_URL + "PublicApi/validateCode";
	public static String EASY_LOGIN = MAIN_URL + "PublicApi/easy_login";

	public static String SMS = MAIN_URL + "PublicApi/sms";//短信接口
	public static String MOBILE_LOGIN = MAIN_URL + "PublicApi/tel_reg";//手机登录
	public static String WX_LOGIN = MAIN_URL + "PublicApi/wx_login";//微信登录
	public static String WX_REGIST = MAIN_URL + "PublicApi/wx_reg";//微信绑定
	public static String EDIT_PWD = MAIN_URL + "PublicApi/uppass";//修改密码
	public static String OPEN_BONUS = MAIN_URL + "PublicApi/bonus_open";//打开红包

	public static String ABOUT_ME = MAIN_URL + "PublicApi/my_info";//关于我们
	public static String QUESTION = MAIN_URL + "PublicApi/cjwt";//常见问题
	public static String FULI = MAIN_URL + "PublicApi/welfare";//发福利
	public static String CHECK_UPDATE = MAIN_URL + "PublicApi/app_update";//检查更新
	public static String JPUSH_RECEIVE = MAIN_URL + "Api/update_clientid";//上传jpush id
	public static String SUPER_RETURN = MAIN_URL + "Api/supermoney";//超级返

}

package com.whotw.security.domain;

/**
 * @author EdisonXu
 * @date 2019/7/11
 */

public interface SecurityConstants {

    /**
     * 登录页面
     */
    String LOGIN_PAGE = "/login.html";
    /**
     * 默认的微信小程序登录请求处理url
     */
    String WX_MP_TOKEN_URL = "/oauth/wxmp/token";
    /**
     * 默认的OPENID登录请求处理url
     */
    String OPENID_TOKEN_URL = "/oauth/openId/token";
    /**
     * 手机登录URL
     */
    String MOBILE_TOKEN_URL = "/oauth/mobile/token";

    /**
     * 登出URL
     */
    String LOGOUT_URL = "/oauth/remove/token";
    /**
     * 缓存client details的redis key 前缀
     */
    String CACHE_CLIENT_KEY = "oauth_client_details";
    /**
     * 以微信OpenId作为缓存user details的redis key 前缀
     */
    String CACHE_WX_OPEN_ID_UD_KEY = "oauth_account_details_wx_open_id";
    /**
     * 以微信UnionId作为缓存user details的redis key 前缀
     */
    String CACHE_WX_UNION_ID_UD_KEY = "oauth_account_details_wx_union_id";
    /**
     * 微信session key默认3天过期
     */
    long WX_SESSION_KEY_EXPIRE_TIME_IN_DAYS = 3;
    /**
     * 资源服务器默认bean名称
     */
    String RESOURCE_SERVER_CONFIGURER = "resourceServerConfigurerAdapter";
    /**
     * Map中保存Account的Key
     */
    String ACCOUNT_KEY = "account";
    String CLIENT_ID_KEY = "client_id";
    //黑名单数据
    String BLACKLIST = "blacklist";
    //登陆数据
    String LOGIN = "login";

    /**
     * 客户端模式
     */
    String GRANT_TYPE_CLIENT_CREDENTIALS = "client_credentials";

    String GRANT_TYPE_MOBILE_PASSWORD = "mobile_password";

    String GRANT_TYPE_AUTHED_MOBILE = "authed_mobile";

    String GRANT_TYPE_WX_AUTH = "wx_auth";

    String CACHE_ACCOUNT_DETAILS_BY_MOBILE = "account_details_mobile";

    String CACHE_DISABLED_MOBILE_ACCOUNT = "disabled_mobile_account";

    String CACHE_ACCOUNT_DETAILS_BY_OPEN_ID = "account_details_open_id";

    String CACHE_ACCOUNT_DETAILS_BY_UNION_ID = "account_details_union_id";

    String CACHE_FREE_USER_DETAILS_BY_OPEN_ID = "free_user_details_open_id";

    String CACHE_FREE_USER_DETAILS_BY_UNION_ID = "free_user_details_union_id";

    String HEADER_KEY_USER_ID = "userId";

    String HEADER_KEY_USER_NAME = "userFullName";

    String HEADER_KEY_ACCOUNT_ID = "accountId";

    String HEADER_KEY_INSTITUTION_ID = "institutionId";

    String HEADER_KEY_ACCOUNT_PERMISSION = "account_permission_sum";

    String HEADER_KEY_INST_PERMISSION = "inst_service_permission_sum";

    String HEADER_KEY_INNER_ACCESS = "xsh_inner_access";

    String HEADER_KEY_AUTHORIZATION = "Authorization";

    String BEARER_TYPE = "Bearer";

    String OAUTH2_TYPE = "OAuth2";

    String EXP = "exp";

    String HEADER_KEY_MOBILE = "mobile";

    String HEADER_KEY_SYSTEM_ROLE = "systemRole";

    String ACCOUNT_BOUND = "account_bound";
}

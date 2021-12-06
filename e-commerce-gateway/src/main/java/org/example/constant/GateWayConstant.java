package org.example.constant;

/**
 * @author zhoudashuai
 * @date 2021年12月06日 11:36 下午
 * 网关常量定义
 */
public class GateWayConstant {
    /** 登录的uri */
    public static final String LOGIN_URI="/e-commerce/login";

    /** 注册的URI*/
    public static final String REGISTER_URI="/e-commerce/register";
    /** 去授权中心拿到登录token的uri格式化接口*/
    public static final String AUTHORITY_CENTER_TOKEN_URI_FORMAT=
            "http://%s:%s/ecommerce-authority-center/authority/token";
    /** 去授权中心注册并拿到 token 的uri格式化接口*/
    public static final String AUTHORITY_CENTER_REGISTER_URI_FORMAT=
            "http://%s:%s/ecommerce-authority-center/authority/register";

}

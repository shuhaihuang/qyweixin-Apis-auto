package com.wechat.apiobject;

import static io.restassured.RestAssured.given;

/**
 * @Author sean
 * @Date 2020/12/27 13:22
 * @Version 1.0
 */
public class TokenHelper {
    public static String getAccessToken(){
        String accessToken = given().log().all()
                .when()
                .param("corpid","ww3d52506992467125")
                .param("corpsecret","UKp_qRLzieLoqJKfbOaGwSvD4sv0TevgLoW6mHaV-lM")
                //通讯录同步助手secret ： UKp_qRLzieLoqJKfbOaGwSvD4sv0TevgLoW6mHaV-lM
                .get("https://qyapi.weixin.qq.com/cgi-bin/gettoken")
                .then().log().all()
                .extract().response().path("access_token");
        return accessToken;
    }
    public static String getAppAccessToken(){
        String appAccessToken = given().log().all()
                .when()
                .param("corpid","ww3d52506992467125")
                .param("corpsecret","_ePhi_mamku3D9bjNQQanUVFBTM2xiWno4eZeiQdHoc")
                //自创建应用SeanCom secret : _ePhi_mamku3D9bjNQQanUVFBTM2xiWno4eZeiQdHoc
                .get("https://qyapi.weixin.qq.com/cgi-bin/gettoken")
                .then().log().all()
                .extract().response().path("access_token");
        return appAccessToken;
    }
}

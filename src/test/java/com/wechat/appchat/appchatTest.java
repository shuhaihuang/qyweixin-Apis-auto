package com.wechat.appchat;


import com.wechat.apiobject.AppChatObject;
import com.wechat.apiobject.TokenHelper;
import com.wechat.utils.FakerUtils;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @Author sean
 * @Date 2020/12/28 22:24
 * @Version 1.0
 */
public class appchatTest {
    private static final Logger logger = LoggerFactory.getLogger(appchatTest.class);
    static String appAccessToken;
    static String accessToken;

    @BeforeAll
    public static void getAccessToken(){
        accessToken = TokenHelper.getAccessToken();
        appAccessToken = TokenHelper.getAppAccessToken();
        logger.info(appAccessToken);
        logger.info(accessToken);
    }
    @DisplayName("创建群聊")
    @Test
    @Order(1)
    void createAppChat(){
        List userlist = AppChatObject.createUserList(accessToken);
        String name = "AppChatName"+FakerUtils.getTimeStamp();
        String chatId = "chatid"+FakerUtils.getTimeStamp();
        Response createAppChatResponse = AppChatObject.createAppChat(userlist,name,chatId,appAccessToken);
        assertEquals("0",createAppChatResponse.path("errcode").toString());
    }
    @DisplayName("修改群聊")
    @Test
    @Order(2)
    void updateAppChat(){
        String name = "AppChatNew"+FakerUtils.getTimeStamp();
        String chatId = AppChatObject.createAppChatId(appAccessToken,accessToken);
        Response updateAppChatResponse = AppChatObject.updateAppChat(chatId,name,appAccessToken);
        assertEquals("0",updateAppChatResponse.path("errcode").toString());
    }
    @DisplayName("获取群聊")
    @Test
    @Order(3)
    void getAppChat() {
        String chatid = AppChatObject.createAppChatId(appAccessToken,accessToken);
        Response getAppChatResponse = AppChatObject.getAppChat(chatid,appAccessToken);
        assertEquals("0",getAppChatResponse.path("errcode").toString());
    }
    @DisplayName("发送群聊消息")
    @Test
    @Order(4)
    void sendAppChatMsg() {
        String chatid = AppChatObject.createAppChatId(appAccessToken,accessToken);
        Response getAppChatResponse = AppChatObject.appChatSendMsg(chatid,appAccessToken);
        assertEquals("0",getAppChatResponse.path("errcode").toString());
    }
}

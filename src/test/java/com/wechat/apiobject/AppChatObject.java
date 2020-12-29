package com.wechat.apiobject;

import com.wechat.utils.FakerUtils;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

/**
 * @Author sean
 * @Date 2020/12/28 22:25
 * @Version 1.0
 */
public class AppChatObject {
    public static Response createAppChat(List userlist,String name,String chatId,String accessToken){
        String createBody = "{\n"+
                "   \"name\": \""+name+"\",\n"+
                "   \"userlist\": "+userlist+",\n"+
                "   \"chatid\" : \""+chatId+"\""+
                "   \n}";
        Response creatAppChatResponse = given().log().all()
                .contentType("application/json")
                .body(createBody)
                .post("https://qyapi.weixin.qq.com/cgi-bin/appchat/create?access_token="+accessToken+"")
                .then()
                .log().all()
                .extract().response();
        return creatAppChatResponse;
    }
    public static List createUserList(String accessToken){
        List<String> userlist= new ArrayList<>();
        for(int i = 0; i < 3; i++){
            String userid = UserObject.createUserId(accessToken);
            userlist.add("\""+userid+"\"");
        }
        return userlist;
    }
    public static String createAppChatId(String appAccessToken,String accessToken){
        List userlist = AppChatObject.createUserList(accessToken);
        String name = "AppChatName"+ FakerUtils.getTimeStamp();
        String chatId = "chatid"+FakerUtils.getTimeStamp();
        Response createAppChatResponse = AppChatObject.createAppChat(userlist,name,chatId,appAccessToken);
        return chatId;
    }
    public static Response updateAppChat(String chatid,String name,String accessToken){
        String updateBody = "{\n"+
                "   \"chatid\": \""+chatid+"\",\n"+
                "   \"name\": \""+name+"\""+
                "   \n}";
        Response updateAppChatResponse=given().log().all()
                .contentType("application/json")
                .body(updateBody)
                .post("https://qyapi.weixin.qq.com/cgi-bin/appchat/update?access_token="+accessToken+"")
                .then()
                .log().all()
                .extract().response();
        return updateAppChatResponse;
    }
    public static Response getAppChat(String chatid,String accessToken){
        Response getAppChatResponse=given().log().all()
                .contentType("application/json")
                .param("chatid",chatid)
                .get("https://qyapi.weixin.qq.com/cgi-bin/appchat/get?access_token="+accessToken)
                .then()
                .log().all()
                .extract().response();
        return getAppChatResponse;
    }
    public static Response appChatSendMsg(String chatid,String accessToken){
        String SendContents = "{\n"+
                "   \"chatid\": \""+chatid+"\","+
                "   \"msgtype\":\"text\","+
                "   \"text\":{"+
                "   \"content\" : \"你的快递已到\\n请携带工卡前往邮件中心领取\""+
                    "},"+
                "   \"safe\":0"+
                "   \n}";
        Response appChatSendMsgResponse=given().log().all()
                .contentType("application/json")
                .body(SendContents)
                .post("https://qyapi.weixin.qq.com/cgi-bin/appchat/send?access_token="+accessToken+"")
                .then()
                .log().all()
                .extract().response();
        return appChatSendMsgResponse;
    }
}

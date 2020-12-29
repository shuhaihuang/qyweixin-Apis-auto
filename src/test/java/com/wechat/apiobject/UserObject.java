package com.wechat.apiobject;

import com.wechat.utils.FakerUtils;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

/**
 * @Author sean
 * @Date 2020/12/27 22:02
 * @Version 1.0
 */
public class UserObject {
    public static Response createUser(String accessToken,String userid,String name,String phoneNum){
        String createBody = "{\n"+
                "   \"userid\": \""+userid+"\",\n"+
                "   \"name\": \""+name+"\",\n"+
                "   \"department\": [1, 2],\n"+
                "   \"mobile\": \""+phoneNum+"\""+
//                "   \"email\": \"shuhai@163.com\""+
                "\n}";
        Response creatUsrResponse=given().log().all()
                .contentType("application/json")
                .body(createBody)
                .post("https://qyapi.weixin.qq.com/cgi-bin/user/create?access_token="+accessToken+"")
                .then()
                .log().body()
                .extract().response();
        return creatUsrResponse;
    }
    public static String createUserId(String accessToken){
        String Userid = "id" + FakerUtils.getTimeStamp();
        String Name = "name" + FakerUtils.getTimeStamp();
        String phoneNum = FakerUtils.getTel();
        Response createUsrResponse = UserObject.createUser(accessToken,Userid,Name,phoneNum);
        return Userid;
    }
    public static Response getUser(String accessToken,String userid){
        Response getUsrResponse=given().log().all()
                .param("access_token",accessToken)
                .param("userid",userid)
                .get("https://qyapi.weixin.qq.com/cgi-bin/user/get")
                .then()
                .log().body()
                .extract().response();
        return getUsrResponse;
    }
    public static Response updateUser(String accessToken,String userid,String name){
        String updateBody = "{\n"+
                "\"userid\": \""+userid+"\","+
                "\"name\": \""+name+"\","+
                "\"department\": [1]"+
                "\n}";

        Response updateUsrResponse=given().log().all()
                .contentType("application/json")
                .body(updateBody)
                .post("https://qyapi.weixin.qq.com/cgi-bin/user/update?access_token="+accessToken+"")
                .then()
                .log().body()
                .extract().response();
        return updateUsrResponse;
    }
    public static Response deleteUser(String accessToken,String userid){
        Response deleteUsrResponse=given().log().all()
                .param("userid",userid)
                .get("https://qyapi.weixin.qq.com/cgi-bin/user/delete?access_token="+accessToken+"")
                .then()
                .log().body()
                .extract().response();
        return deleteUsrResponse;
    }
    public static Response getUserSimpleList(String accessToken,String departmentId){
        Response deleteUsrResponse=given().log().all()
                .param("department_id",departmentId)
                .param("fetch_child",1)
                .get("https://qyapi.weixin.qq.com/cgi-bin/user/simplelist?access_token="+accessToken+"")
                .then()
                .log().body()
                .extract().response();
        return deleteUsrResponse;
    }

}

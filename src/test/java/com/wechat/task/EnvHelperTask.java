package com.wechat.task;

import com.wechat.apiobject.DepartMentObject;
import com.wechat.apiobject.UserObject;
import com.wechat.department.Demo_05_softassert;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * @Author sean
 * @Date 2020/12/27 20:04
 * @Version 1.0
 */
public class EnvHelperTask {
    private static final Logger logger = LoggerFactory.getLogger(Demo_05_softassert.class);
    public static void clearDepartMentTask(String accessToken){
        Response listResponse = DepartMentObject.listDepartMent("1",accessToken);
        ArrayList<Integer> departmentlist = listResponse.path("department.id");
        for(int departMentId : departmentlist){
            if(1==departMentId){
                continue;
            }
            Response deleteResponse = DepartMentObject.deletDepartMent(departMentId+"",accessToken);
        }
    }
    public static void clearAllDepartUser(String accessToken){
        Response getUsrListResponse = UserObject.getUserSimpleList(accessToken,"1");
        ArrayList<String> userList = getUsrListResponse.path("userlist.userid");
        for(String userid : userList){
            if(userid.equals("HuangXueShen")){
                continue;
            }
            Response deleteUsrResponse = UserObject.deleteUser(accessToken,userid);
        }
    }
}

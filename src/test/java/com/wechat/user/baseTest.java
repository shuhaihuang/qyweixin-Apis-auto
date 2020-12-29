package com.wechat.user;

import com.wechat.apiobject.TokenHelper;
import com.wechat.apiobject.UserObject;
import com.wechat.department.Demo_05_softassert;
import com.wechat.task.EnvHelperTask;
import com.wechat.utils.FakerUtils;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @Author sean
 * @Date 2020/12/27 22:35
 * @Version 1.0
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class baseTest {
    private static final Logger logger = LoggerFactory.getLogger(Demo_05_softassert.class);
    static String accessToken;

    @BeforeAll
    public static void getAccessToken(){
        accessToken = TokenHelper.getAccessToken();
        logger.info(accessToken);
    }
    @DisplayName("清理环境")
    @AfterEach
    @BeforeEach
    void clearAllDepartUser(){
        EnvHelperTask.clearAllDepartUser(accessToken);
    }

    @DisplayName("创建成员")
    @Test
    @Order(1)
    void createUser(){
        String Userid = "id" + FakerUtils.getTimeStamp();
        String Name = "name" + FakerUtils.getTimeStamp();
        String phoneNum = FakerUtils.getTel();
        Response createUsrResponse = UserObject.createUser(accessToken,Userid,Name,phoneNum);
        assertEquals("0",createUsrResponse.path("errcode").toString());
    }
    @DisplayName("读取成员")
    @Test
    @Order(2)
    void getUser(){
        String Userid = UserObject.createUserId(accessToken);
        //创建成员后，读取成员
        Response getUsrResponse = UserObject.getUser(accessToken,Userid);
        assertEquals("0",getUsrResponse.path("errcode").toString());
    }
    @DisplayName("更新成员")
    @Test
    @Order(3)
    void updateUser(){
        String Userid = UserObject.createUserId(accessToken);
        String Name = "NewName" + FakerUtils.getTimeStamp();
        //创建成员后，更新成员
        Response updateUsrResponse = UserObject.updateUser(accessToken,Userid,Name);
        assertEquals("0",updateUsrResponse.path("errcode").toString());
    }
    @DisplayName("删除成员")
    @Test
    @Order(4)
    void deleteUser(){
        String Userid = UserObject.createUserId(accessToken);
        //创建成员后，删除成员
        Response deleteUsrResponse = UserObject.deleteUser(accessToken,Userid);
        assertEquals("0",deleteUsrResponse.path("errcode").toString());
    }
}

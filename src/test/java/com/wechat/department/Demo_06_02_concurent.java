package com.wechat.department;

import com.wechat.apiobject.DepartMentObject;
import com.wechat.apiobject.TokenHelper;
import com.wechat.task.EnvHelperTask;
import com.wechat.utils.FakerUtils;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
/**
 * 并发进行 创建部门和更新部门，保证互不影响，并发多接口测试
 */

/**
 * @Author sean
 * @Date 2020/12/21 21:41
 * @Version 1.0
 */

public class Demo_06_02_concurent {
    private static final Logger logger = LoggerFactory.getLogger(Demo_06_02_concurent.class);
    static String accessToken;

    @BeforeAll
    public static void getAccessToken(){
        accessToken = TokenHelper.getAccessToken();
        logger.info(accessToken);
    }

    @DisplayName("创建部门")
    @Test
    @RepeatedTest(10)
    void createDepartment() {
        String backendStr = Thread.currentThread().getId()+FakerUtils.getTimeStamp();
        String name = "name" + backendStr;
        String enName = "enName" + backendStr;
        Response createResponse = DepartMentObject.creatDepartMent(name,enName,accessToken);
        assertEquals("0",createResponse.path("errcode").toString());
    }
    @DisplayName("修改部门")
    @Test
    @RepeatedTest(10)
    void updateDepartment() {
        String backendStr = Thread.currentThread().getId()+FakerUtils.getTimeStamp();
        String createName = "name" + backendStr;
        String createEnName = "enName" + backendStr;
        Response createResponse = DepartMentObject.creatDepartMent(createName,createEnName,accessToken);
        //先创建部门，再更新
        String departMentId= createResponse.path("id")!=null ? createResponse.path("id").toString():null;
        String updateName = "name" + backendStr;
        String updateEnName = "enName" + backendStr;
        Response updateResponse= DepartMentObject.updateDepartMent(updateName,updateEnName,departMentId,accessToken);
        assertEquals("0",updateResponse.path("errcode").toString());
    }

}

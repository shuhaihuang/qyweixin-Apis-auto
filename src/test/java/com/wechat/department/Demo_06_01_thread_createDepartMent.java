package com.wechat.department;

import com.wechat.apiobject.DepartMentObject;
import com.wechat.apiobject.TokenHelper;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.parallel.Execution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.parallel.ExecutionMode.CONCURRENT;

/**
 * 验证加了分布式锁，验证多线程创建部门，只有一个成功
 * 只并发一个接口
 */

/**
 * @Author sean
 * @Date 2020/12/27 20:18
 * @Version 1.0
 */
public class Demo_06_01_thread_createDepartMent {
    private static final Logger logger = LoggerFactory.getLogger(Demo_06_01_thread_createDepartMent.class);
    static String accessToken;

    @BeforeAll
    public static void getAccessToken() throws Exception {
        accessToken = TokenHelper.getAccessToken();
        logger.info(accessToken);

    }

    @DisplayName("创建部门")
    @RepeatedTest(100)
    @Execution(CONCURRENT)
    void createDepartment() {
        String creatName= "name1234567";
        String creatEnName="en_name1234567";

        Response creatResponse = DepartMentObject.creatDepartMent(creatName,creatEnName,accessToken);
        assertEquals("0",creatResponse.path("errcode").toString());
    }
}

package com.wechat.department;

import com.wechat.apiobject.DepartMentObject;
import com.wechat.apiobject.TokenHelper;
import com.wechat.task.EnvHelperTask;
import com.wechat.utils.FakerUtils;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
/**
 * 1 基础功能，创建、修改、查询、删除
 * 2 进行优化，方法间解耦，每个方法可以独立运行
 * 3 使用时间戳命名法避免数据重复造成的报错
 * 4 进行优化，每次执行方法前后对历史数据清理，确保每次脚本执行时的环境一致
 * 5 进行优化，脚本分层，减少了重复代码，提高了代码复用率，从而减少维护成本
 */

/**
 * @Author sean
 * @Date 2020/12/21 21:41
 * @Version 1.0
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Demo_04_Layer {
    private static final Logger logger = LoggerFactory.getLogger(Demo_04_Layer.class);
    static String accessToken;

    @BeforeAll
    public static void getAccessToken(){
        accessToken = TokenHelper.getAccessToken();
        logger.info(accessToken);
    }
    @AfterEach
    @BeforeEach
    void clearDepartment(){
        EnvHelperTask.clearDepartMentTask(accessToken);
//        Response listresponse = DepartMentObject.listDepartMent("1",accessToken);
//        ArrayList<Integer> departmentlist = listresponse.path("department.id");
//        for(int departMentId : departmentlist){
//            if(1==departMentId){
//                continue;
//            }
//            Response deleteresponse = DepartMentObject.deletDepartMent(departMentId+"",accessToken);
//        }
    }

    @DisplayName("创建部门")
    @Test
    @Order(1)
    void createDepartment() throws InterruptedException {
        String name = "name" + FakerUtils.getTimeStamp();
        String enName = "enName" + FakerUtils.getTimeStamp();
        Response createResponse = DepartMentObject.creatDepartMent(name,enName,accessToken);
        assertEquals("created",createResponse.path("errmsg").toString());
        Thread.sleep(10000);
    }
    @DisplayName("修改部门")
    @Test
    @Order(2)
    void updateDepartment() throws InterruptedException {
        String updateName = "name" + FakerUtils.getTimeStamp();
        String updateEnName = "enName" + FakerUtils.getTimeStamp();
        String departMentId = DepartMentObject.creatDepartMent(accessToken);
        //先创建部门，再更新
        Response updateResponse= DepartMentObject.updateDepartMent(updateName,updateEnName,departMentId,accessToken);
        assertEquals("0",updateResponse.path("errcode").toString());
        Thread.sleep(10000);
    }
    @DisplayName("查询部门")
    @Test
    @Order(3)
    void listDepartment() {
        String departMentId = DepartMentObject.creatDepartMent(accessToken);
        //创建部门，查询
        Response listResponse = DepartMentObject.listDepartMent(departMentId,accessToken);
        assertEquals("0",listResponse.path("errcode").toString());
        assertEquals(departMentId,listResponse.path("department.id[0]").toString());
    }
    @DisplayName("删除部门")
    @Test
    @Order(4)
    void deleteDepartment() {
        String departMentId = DepartMentObject.creatDepartMent(accessToken);
        //创建部门，删除
        Response deleteResponse = DepartMentObject.deletDepartMent(departMentId,accessToken);
        assertEquals("0", deleteResponse.path("errcode").toString());
    }
}

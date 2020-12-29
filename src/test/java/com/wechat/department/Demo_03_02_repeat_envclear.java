package com.wechat.department;

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
 */

/**
 * @Author sean
 * @Date 2020/12/21 21:41
 * @Version 1.0
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Demo_03_02_repeat_envclear {
    private static final Logger logger = LoggerFactory.getLogger(Demo_03_02_repeat_envclear.class);

    static String accessToken;

    @BeforeAll
    public static void getAccessToken(){
        accessToken = given().log().all()
                .when()
                .param("corpid","ww3d52506992467125")
                .param("corpsecret","UKp_qRLzieLoqJKfbOaGwSvD4sv0TevgLoW6mHaV-lM")
                .get("https://qyapi.weixin.qq.com/cgi-bin/gettoken")
                .then().log().all()
                .extract().response().path("access_token");
    }
    @AfterEach
    @BeforeEach
    void clearDepartment(){
        Response listresponse =given().log().all()
                .when()
                .param("id",1)
                .get("https://qyapi.weixin.qq.com/cgi-bin/department/list?access_token="+accessToken)
                .then()
                .log().body()
                .extract().response();
        ArrayList<Integer> departmentlist = listresponse.path("department.id");
        for(int departMentId : departmentlist){
            if(1==departMentId){
                continue;
            }
            Response deleteresponse = given().log().all()
                    .contentType("application/json")
                    .param("access_token", accessToken)
                    .param("id", departMentId)
                    .get("https://qyapi.weixin.qq.com/cgi-bin/department/delete")
                    .then()
                    .log().body()
                    .extract().response();
        }
    }

    @DisplayName("创建部门")
    @Test
    @Order(1)
    void createDepartment() throws InterruptedException {
        String name = "name" + FakerUtils.getTimeStamp();
        String enName = "enName" + FakerUtils.getTimeStamp();
        String createbody = "{\n" +
               "\"name\": \""+name+"\",\n"  +
                "\"name_en\": \""+enName+"\",\n" +
                "\"parentid\": 1,\n" +
                "\"order\": 1\n" +
                "}\n";
        Response createresponse = given().log().all()
                .contentType("application/json")
                .body(createbody)
                .post("https://qyapi.weixin.qq.com/cgi-bin/department/create?access_token="+accessToken)
                .then().log().all()
                .extract().response();
        assertEquals("created",createresponse.path("errmsg").toString());
        String departMentId = createresponse.path("id")!=null ? createresponse.path("id").toString():null;
        logger.info(accessToken);
        Thread.sleep(10000);
    }
    @DisplayName("修改部门")
    @Test
    @Order(2)
    void updateDepartment() throws InterruptedException {
        String name = "name" + FakerUtils.getTimeStamp();
        String enName = "enName" + FakerUtils.getTimeStamp();
        String createbody = "{\n" +
                "\"name\": \""+name+"\",\n"  +
                "\"name_en\": \""+enName+"\",\n" +
                "\"parentid\": 1,\n" +
                "\"order\": 1\n" +
                "}\n";
        Response createresponse = given().log().all()
                .contentType("application/json")
                .body(createbody)
                .post("https://qyapi.weixin.qq.com/cgi-bin/department/create?access_token="+accessToken)
                .then().log().all()
                .extract().response();
        String departMentId = createresponse.path("id")!=null ? createresponse.path("id").toString():null;
        Thread.sleep(10000);
        //先创建部门，再更新
        String updatebody ="{\n" +
                "   \"id\": "+departMentId+",\n" +
                "\"name\": \""+name+"new\",\n"  +
                "\"name_en\": \""+enName+"new\",\n" +
                "   \"order\": 1\n" +
                "}\n";
        Response updateresponse=given().log().all()
                .contentType("application/json")
                .body(updatebody)
                .post("https://qyapi.weixin.qq.com/cgi-bin/department/update?access_token="+accessToken+"")
                .then()
                .log().body()
                .extract().response();
        assertEquals("0",updateresponse.path("errcode").toString());
        Thread.sleep(10000);
    }

    @DisplayName("查询部门")
    @Test
    @Order(3)
    void listDepartment() throws InterruptedException {
        String name = "name" + FakerUtils.getTimeStamp();
        String enName = "enName" + FakerUtils.getTimeStamp();
        String createbody = "{\n" +
                "\"name\": \""+name+"\",\n"  +
                "\"name_en\": \""+enName+"\",\n" +
                "\"parentid\": 1,\n" +
                "\"order\": 1\n" +
                "}\n";
        Response createresponse = given().log().all()
                .contentType("application/json")
                .body(createbody)
                .post("https://qyapi.weixin.qq.com/cgi-bin/department/create?access_token="+accessToken)
                .then().log().all()
                .extract().response();
        String departMentId = createresponse.path("id").toString();
        //创建部门，查询
        Response listresponse =given().log().all()
                .when()
                .param("id",departMentId)
                .get("https://qyapi.weixin.qq.com/cgi-bin/department/list?access_token="+accessToken)
                .then()
                .log().body()
                .extract().response();
        assertEquals("0",listresponse.path("errcode").toString());
        assertEquals(departMentId,listresponse.path("department.id[0]").toString());
        Thread.sleep(10000);
    }
    @DisplayName("删除部门")
    @Test
    @Order(4)
    void deleteDepartment() {
        String name = "name" + FakerUtils.getTimeStamp();
        String enName = "enName" + FakerUtils.getTimeStamp();
        String createbody = "{\n" +
                "\"name\": \""+name+"\",\n"  +
                "\"name_en\": \""+enName+"\",\n" +
                "\"parentid\": 1,\n" +
                "\"order\": 1\n" +
                "}\n";
        Response createresponse = given().log().all()
                .contentType("application/json")
                .body(createbody)
                .post("https://qyapi.weixin.qq.com/cgi-bin/department/create?access_token="+accessToken)
                .then().log().all()
                .extract().response();
        String departMentId = createresponse.path("id").toString();
        //创建部门，删除
        Response deleteresponse = given().log().all()
                .contentType("application/json")
                .param("access_token", accessToken)
                .param("id", departMentId)
                .get("https://qyapi.weixin.qq.com/cgi-bin/department/delete")
                .then()
                .log().body()
                .extract().response();
        assertEquals("0", deleteresponse.path("errcode").toString());
    }
}

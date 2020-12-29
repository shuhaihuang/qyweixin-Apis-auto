package com.wechat.department;

import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
/**
 * 1 基础功能，创建、修改、查询、删除
 */

/**
 * @Author sean
 * @Date 2020/12/21 21:41
 * @Version 1.0
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Demo_01_Base {
    private static final Logger logger = LoggerFactory.getLogger(Demo_01_Base.class);

    static String accessToken;
    static String departMentId;
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
    @DisplayName("创建部门")
    @Test
    @Order(1)
    void createDepartment() throws InterruptedException {
        String body = "{\n" +
               "\"name\": \"广州研发中心2\",\n"  +
                "\"name_en\": \"RDGZn2\",\n" +
                "\"parentid\": 1,\n" +
                "\"order\": 1\n" +
                "}\n";
        Response response = given().log().all()
                .contentType("application/json")
                .body(body)
                .post("https://qyapi.weixin.qq.com/cgi-bin/department/create?access_token="+accessToken)
                .then().log().all()
                .extract().response();
        assertEquals("created",response.path("errmsg").toString());
        departMentId = response.path("id").toString();
        logger.info(accessToken);
        Thread.sleep(10000);
    }
    @DisplayName("修改部门")
    @Test
    @Order(2)
    void updateDepartment() throws InterruptedException {

        String body ="{\n" +
                "   \"id\": "+departMentId+",\n" +
                "   \"name\": \"更新部门名\",\n" +
                "   \"name_en\": \"updatedepName\",\n" +
                "   \"order\": 1\n" +
                "}\n";
        Response response=given().log().all()
                .contentType("application/json")
                .body(body)
                .post("https://qyapi.weixin.qq.com/cgi-bin/department/update?access_token="+accessToken+"")
                .then()
                .log().body()
                .extract().response();
        assertEquals("0",response.path("errcode").toString());
        Thread.sleep(10000);
    }

    @DisplayName("查询部门")
    @Test
    @Order(3)
    void listDepartment() throws InterruptedException {

        Response response =given().log().all()
                .when()
                .param("id",departMentId)
                .get("https://qyapi.weixin.qq.com/cgi-bin/department/list?access_token="+accessToken)
                .then()
                .log().body()
                .extract().response();
        assertEquals("0",response.path("errcode").toString());
        assertEquals(departMentId,response.path("department.id[0]").toString());
        Thread.sleep(10000);
    }
    @DisplayName("删除部门")
    @Test
    @Order(4)
    void deleteDepartment() {
        Response response = given().log().all()
                .contentType("application/json")
                .param("access_token", accessToken)
                .param("id", departMentId)
                .get("https://qyapi.weixin.qq.com/cgi-bin/department/delete")
                .then()
                .log().body()
                .extract().response();
        assertEquals("0", response.path("errcode").toString());
    }
}

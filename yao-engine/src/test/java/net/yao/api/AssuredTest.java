package net.yao.api;


import com.jayway.jsonpath.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AssuredTest {
    @Test
    public void testBase() {
        RestAssured.given()
                .param("id", 1)
                .when()
                .get("http://127.0.0.1:8082/api/v2/test/detail")
                .then()
                .log()
                .all()
                .statusCode(200);
    }

    @Test
    public void testPostForm(){

        RestAssured.given()
                .formParam("mail","haha")
                .formParam("pwd","1234")
                .when()
                .post("http://127.0.0.1:8082/api/v1/test/login_form")
                .then()
                .log()
                .all()
                .statusCode(200);

    }


    @Test
    public void testPostJsonHeader(){
        Map<String,String> map = new HashMap<>();
        map.put("title","yao");
        RestAssured.given()
                .header("token","3a74fbbeb3114b38bc0f5b61296e8835")
                //.header("Content-Type","application/json")
                .contentType(ContentType.JSON)
                .body(map)
                .when()
                .post("http://127.0.0.1:8082/api/v1/test/buy")
                .then()
                .log()
                .all()
                .statusCode(200);

    }


    @Test
    public void testAssert(){
        Map<String,String> map = new HashMap<>();
        map.put("title","yao");
        RestAssured.given()
                .header("token","3a74fbbeb3114b38bc0f5b61296e8835")
                //.header("Content-Type","application/json")
                .contentType(ContentType.JSON)
                .body(map)
                .when()
                .post("http://127.0.0.1:8082/api/v1/test/buy")
                .then()
                .log()
                .all()
                .statusCode(200)
                .body("code",equalTo(0));

    }

    @Test
    public void testJsonPath(){
        Map<String,String> map = new HashMap<>();
        map.put("title","yao");
        Response response = RestAssured.given()
                .header("token", "3a74fbbeb3114b38bc0f5b61296e8835")
                //.header("Content-Type","application/json")
                .contentType(ContentType.JSON)
                .body(map)
                .when()
                .post("http://127.0.0.1:8082/api/v1/test/buy")
                .then()
                .log()
                .all()
                .statusCode(200).extract().response();
        String responseString = response.asString();
        System.out.println(responseString);
        String read = JsonPath.read(responseString, "$.data.title");
        System.out.println(read);
    }

}

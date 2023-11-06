package edu.uoc.epcsd.unitTest;


import org.hamcrest.Matchers;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.ResponseSpecification;

public class AssuredTest {
    ResponseSpecification responseSpec = null;
    @BeforeClass
    public void setupResponseSpec(int code) {
        responseSpec = RestAssured.expect();
        responseSpec.contentType(ContentType.JSON);
        responseSpec.statusCode(code);
        responseSpec.time(Matchers.lessThan(5000L));
        responseSpec.statusLine("HTTP/1.1 "+code+" ");
    }
    @Test
    public void GetUsers_CheckJsonWithData() {
        setupResponseSpec(200);
        RestAssured.baseURI = "http://localhost:18084/users";
        RestAssured.given()
                .when().get()
                .then().spec(responseSpec).body("size()",Matchers.greaterThan(0));
    }
    @Test
    public void GetUsers_GetById() {
        setupResponseSpec(200);
        RestAssured.baseURI = "http://localhost:18084/users/1";
        RestAssured.given()
                .when().get()
                .then().spec(responseSpec).body("id",Matchers.comparesEqualTo(1));
    }
    @Test
    public void PutUsers_CreateNew() throws JSONException {
        setupResponseSpec(200);
        JSONObject requestParams = new JSONObject();
        requestParams.put("name", "Pedro");
        requestParams.put("surname", "Lopez");
        requestParams.put("email", "pedrolopez@gmail.com");
        requestParams.put("password", "1234");
        requestParams.put("description", "test description");
        requestParams.put("profile", "1");
        requestParams.put("enterpriseId", "1");
        requestParams.put("disabled", "false");

        RestAssured.baseURI = "http://localhost:18084/users";
        RestAssured.given().header("Content-Type", "application/json").body(requestParams.toString())
                .when().put()
                .then().spec(responseSpec);
    }
    @Test
    public void PostUsers_Edit() throws JSONException {
        setupResponseSpec(201);
        JSONObject requestParams = new JSONObject();
        requestParams.put("id", "0");
        requestParams.put("name", "Pedro");
        requestParams.put("surname", "Gomez");
        requestParams.put("email", "pedrolopez@gmail.com");
        requestParams.put("password", "1234");
        requestParams.put("description", "test description");
        requestParams.put("profile", "1");
        requestParams.put("enterpriseId", "1");
        requestParams.put("disabled", "false");

        RestAssured.baseURI = "http://localhost:18084/users";
        RestAssured.given().header("Content-Type", "application/json").body(requestParams.toString())
                .when().post()
                .then().spec(responseSpec);
    }
}
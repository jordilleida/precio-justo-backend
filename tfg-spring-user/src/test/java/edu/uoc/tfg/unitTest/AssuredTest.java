package edu.uoc.tfg.unitTest;


import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.ResponseSpecification;

public class AssuredTest {
    ResponseSpecification responseSpec = null;
    private static String token;
    @BeforeClass
    public void setupResponseSpec(int code) {
        responseSpec = RestAssured.expect();
        responseSpec.contentType(ContentType.JSON);
        responseSpec.statusCode(code);
        responseSpec.time(Matchers.lessThan(5000L));
        responseSpec.statusLine("HTTP/1.1 "+code+" ");
    }

    @BeforeAll
    public static void loginAndGetToken() {
        JSONObject loginCredentials = new JSONObject();
        try {

            loginCredentials.put("mail", "jordi@uoc.edu");
            loginCredentials.put("password", "123");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(loginCredentials.toString())
                .when()
                .post("http://localhost:18084/login");

        token = response.jsonPath().getString("accessToken");
    }

    @Test
    public void testLogin_Endpoint() {
        assert token != null && !token.isEmpty();
    }
    @Test
    public void GetUsers_Endpoint() {
        setupResponseSpec(200);
        RestAssured.baseURI = "http://localhost:18084/users";

        RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .when().get()
                .then().spec(responseSpec).body("size()",Matchers.greaterThan(0));
    }
    @Test
    public void GetUserByEmail_Endpoint() {
        setupResponseSpec(200);
        RestAssured.baseURI = "http://localhost:18084/users/jordi@uoc.edu";
        RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .when().get()
                .then().spec(responseSpec).body("id",Matchers.comparesEqualTo(1));
    }

    //Este test solo se puede realizar una vez si no se reinician los microservicios ya que el email ya estaria agregado
    @Test
    public void RegisterUser_Endpoint() throws JSONException {
        setupResponseSpec(200);
        JSONObject requestParams = new JSONObject();
        requestParams.put("name", "Pedro");
        requestParams.put("surname", "Lopez");
        requestParams.put("mail", "prueba@uoc.edu");
        requestParams.put("password", "123");

        RestAssured.baseURI = "http://localhost:18084";
        RestAssured.given().header("Content-Type", "application/json")
                .body(requestParams.toString())
                .when().post("/register")
                .then().spec(responseSpec);
    }

}
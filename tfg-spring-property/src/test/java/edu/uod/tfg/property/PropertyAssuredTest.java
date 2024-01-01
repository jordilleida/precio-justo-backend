package edu.uod.tfg.property;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest
class PropertyAssuredTest {

    private static String token;
    private static String adminToken;

    @BeforeAll
    static void setup() {
        // Para pruebas de integración no he encontrado otra forma de conseguir los token para property que previamente consultando
        // el servicio de user ya que hay expired date en los token
        RestAssured.baseURI = "http://localhost:18083";
        token = authenticateUser("pablo@uoc.edu", "123");
        adminToken = authenticateUser("jordi@uoc.edu", "123");
    }

    private static String authenticateUser(String email, String password) {
        String loginRequestBody = "{\"mail\": \"" + email + "\", \"password\": \"" + password + "\"}";

        return given()
                .baseUri("http://localhost:18084")
                .contentType(ContentType.JSON)
                .body(loginRequestBody)
                .when()
                .post("/login")
                .then()
                .statusCode(200)
                .extract()
                .path("accessToken");
    }

    @Test
    void testFindPropertiesEndpoint() {
        given()
                .header("Authorization", "Bearer " + adminToken)
                .when()
                .get("/properties")
                .then()
                .statusCode(anyOf(is(200), is(204)));
    }

    @Test
    void testGetUserPropertiesEndpoint() {
        Long userId = 4L;
        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/properties/" + userId)
                .then()
                .statusCode(anyOf(is(200), is(204)));
    }

    @Test
    void testGetPropertyByIdEndpoint() {
        Long propertyId = 1L;
        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/property/" + propertyId)
                .then()
                .statusCode(anyOf(is(200), is(404)));
    }

    @Test
    void testCreatePropertyEndpoint() {
        String requestBody = "{\"type\":\"VIVIENDA\",\"description\":\"Piso en Barcelona\",\"rooms\":3,\"baths\":2,\"surface\":120.5,\"registryDocumentUrl\":\"https://example.com/registry.pdf\",\"catastralReference\":\"12345BCN\",\"latitude\":41.38879,\"longitude\":2.15899,\"address\":\"Calle Falsa 123, Barcelona\",\"postalCode\":\"08001\",\"city\":\"Barcelona\",\"region\":\"Cataluña\",\"country\":\"España\"}";

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/create")
                .then()
                .statusCode(anyOf(is(200), is(422), is(500)));
    }
    @Test
    void testFindOwnersEndpoint() {
        given()
                .header("Authorization", "Bearer " + adminToken)
                .when()
                .get("/owners")
                .then()
                .statusCode(anyOf(is(200), is(204)));
    }

    @Test
    void testGetPropertiesInAuctionEndpoint() {
        given()
                .when()
                .get("/auction")
                .then()
                .statusCode(anyOf(is(200), is(204)));
    }

    @Test
    void testGetPropertiesPendingValidationEndpoint() {
        given()
                .header("Authorization", "Bearer " + adminToken)
                .when()
                .get("/pending-validation")
                .then()
                .statusCode(anyOf(is(200), is(204)));
    }

    @Test
    void testValidatePropertyEndpoint() {
        Long propertyId = 1L;
        given()
                .header("Authorization", "Bearer " + adminToken)
                .when()
                .put("/validate/" + propertyId)
                .then()
                .statusCode(anyOf(is(200), is(404)));
    }

    @Test
    void testInvalidatePropertyEndpoint() {
        Long propertyId = 1L;
        given()
                .header("Authorization", "Bearer " + adminToken)
                .when()
                .put("/invalidate/" + propertyId)
                .then()
                .statusCode(anyOf(is(200), is(404)));
    }

    @Test
    void testDeletePropertyEndpoint() {
        Long propertyId = 1L;
        given()
                .header("Authorization", "Bearer " + token) // Asegúrate de que este token corresponda a un usuario con rol 'SELLER'
                .when()
                .put("/delete/" + propertyId)
                .then()
                .statusCode(anyOf(is(200), is(404)));
    }

}



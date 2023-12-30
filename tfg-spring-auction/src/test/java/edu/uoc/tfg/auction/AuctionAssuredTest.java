package edu.uoc.tfg.auction;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.emptyArray;
import static org.hamcrest.Matchers.equalTo;

import static org.hamcrest.Matchers.containsString;

import static io.restassured.RestAssured.given;

@SpringBootTest
class AuctionAssuredTest {

    private static String token;

    @BeforeAll
    static void setup() {
        //No he encotrado otra forma de conseguir un token que no expire permanentemente,
        // por lo tanto debe estar el micro de login endendido para conseguir un login valido
        RestAssured.baseURI = "http://localhost:18085";
        token = authenticateUser("pablo@uoc.edu", "123");
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
    void testCreateAuctionEndpoint() {
        String requestBody = "{\"propertyId\": 3, \"initialPrice\": 100}";

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/create")
                .then()
                .statusCode(200)
                .body(containsString("Subasta creada con éxito con un precio de salida de 100"));
    }
    @Test
    void testGetActiveAuctionsEndpoint() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get("/active")
                .then()
                .statusCode(200)
                .body(not(emptyArray()));
    }
    @Test
    void testGetLastAuctionsEndpoint() {
        RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .when()
                .get("/ended")
                .then()
                .statusCode(anyOf(is(200), is(204))) //si no hay subastas finalizadas devolverá 204
                .body(not(emptyArray()));
    }

    /* SOLO COMO ADMIN TOKEN

        @Test
        void testGetAllAuctionsEndpoint() {
            RestAssured.given()
                    .header("Authorization", "Bearer " + token)
                    .contentType(ContentType.JSON)
                    .when()
                    .get("/all")
                    .then()
                    .statusCode(200)
                    .body(not(emptyArray()));
        }

     */
    @Test
    void testGetAuctionByIdEndpoint() {
        Long auctionId = 1L;
        RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .when()
                .get("/" + auctionId)
                .then()
                .statusCode(200)
                .body("id", equalTo(auctionId.intValue()));
    }
    @Test
    void testGetLastAuctionByPropertyIdEndpoint() {
        Long propertyId = 4L; //La que  estoy creando actualmente en el schema.sql
        RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get("/property/" + propertyId + "/last")
                .then()
                .statusCode(200)
                .body("propertyId", equalTo(propertyId.intValue()));
    }
    @Test
    void testGetCurrentAuctionByPropertyIdEndpoint() {
        Long propertyId = 4L; //La que estoy creando actualmente en el schema.sql
        RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get("/property/" + propertyId + "/current")
                .then()
                .statusCode(200)
                .body("propertyId", equalTo(propertyId.intValue()));
    }
    @Test
    void testPlaceBidEndpoint() {

        String requestBody = "{\"auctionId\": 1, \"amount\": 200000}";

        RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/bid")
                .then()
                .statusCode(200)
                .body(containsString("Puja realizada correctamente"));
    }

}


package org.example.petstore;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserApiTest extends BasePetstoreTest
{
    private static final long USER_ID = ThreadLocalRandom.current().nextLong(1_000_000_000L, 9_999_999_999L);
    private static final String USERNAME = "user_" + USER_ID;
    private static final String PASSWORD = "p@ssw0rd";

    @Test
    @Order(1)
    void createUser_returns200()
    {
        Map<String, Object> body = userPayload(USER_ID, USERNAME, "Alice", "Smith");

        given()
                .body(body)
        .when()
                .post("/user")
        .then()
                .statusCode(200)
                .body("message", equalTo(String.valueOf(USER_ID)));
    }

    @Test
    @Order(2)
    void getUserByUsername_returnsCreatedUser()
    {
        given()
                .pathParam("username", USERNAME)
        .when()
                .get("/user/{username}")
        .then()
                .statusCode(200)
                .body("username", equalTo(USERNAME))
                .body("firstName", equalTo("Alice"))
                .body("lastName", equalTo("Smith"));
    }

    @Test
    @Order(3)
    void loginUser_returns200WithSessionMessage()
    {
        given()
                .queryParam("username", USERNAME)
                .queryParam("password", PASSWORD)
        .when()
                .get("/user/login")
        .then()
                .statusCode(200)
                .body("message", containsString("logged in user session"));
    }

    @Test
    @Order(4)
    void logoutUser_returns200()
    {
        given()
        .when()
                .get("/user/logout")
        .then()
                .statusCode(200)
                .body("message", equalTo("ok"));
    }

    @Test
    @Order(5)
    void updateUser_changesFirstName()
    {
        Map<String, Object> body = userPayload(USER_ID, USERNAME, "Alicia", "Smith");

        given()
                .pathParam("username", USERNAME)
                .body(body)
        .when()
                .put("/user/{username}")
        .then()
                .statusCode(200);

        given()
                .pathParam("username", USERNAME)
        .when()
                .get("/user/{username}")
        .then()
                .statusCode(200)
                .body("firstName", equalTo("Alicia"));
    }

    @Test
    @Order(6)
    void createWithList_acceptsArrayOfUsers()
    {
        long batchA = ThreadLocalRandom.current().nextLong(1_000_000_000L, 9_999_999_999L);
        long batchB = ThreadLocalRandom.current().nextLong(1_000_000_000L, 9_999_999_999L);

        List<Map<String, Object>> body = List.of(
                userPayload(batchA, "user_" + batchA, "Bob", "Jones"),
                userPayload(batchB, "user_" + batchB, "Carol", "Lee"));

        given()
                .body(body)
        .when()
                .post("/user/createWithList")
        .then()
                .statusCode(200)
                .body("message", equalTo("ok"));
    }

    @Test
    @Order(7)
    void createWithArray_acceptsArrayOfUsers()
    {
        long batchA = ThreadLocalRandom.current().nextLong(1_000_000_000L, 9_999_999_999L);
        long batchB = ThreadLocalRandom.current().nextLong(1_000_000_000L, 9_999_999_999L);

        List<Map<String, Object>> body = List.of(
                userPayload(batchA, "user_" + batchA, "Dan", "Park"),
                userPayload(batchB, "user_" + batchB, "Eve", "Khan"));

        given()
                .body(body)
        .when()
                .post("/user/createWithArray")
        .then()
                .statusCode(200)
                .body("message", equalTo("ok"));
    }

    @Test
    @Order(8)
    void deleteUser_returns200()
    {
        given()
                .pathParam("username", USERNAME)
        .when()
                .delete("/user/{username}")
        .then()
                .statusCode(200)
                .body("message", equalTo(USERNAME));
    }

    @Test
    @Order(9)
    void getUserByUsername_afterDelete_returns404()
    {
        given()
                .pathParam("username", USERNAME)
        .when()
                .get("/user/{username}")
        .then()
                .statusCode(404)
                .body("message", equalTo("User not found"));
    }

    private static Map<String, Object> userPayload(long id, String username, String first, String last)
    {
        Map<String, Object> payload = new java.util.LinkedHashMap<>();
        payload.put("id", id);
        payload.put("username", username);
        payload.put("firstName", first);
        payload.put("lastName", last);
        payload.put("email", username + "@example.com");
        payload.put("password", PASSWORD);
        payload.put("phone", "555-0100");
        payload.put("userStatus", 1);
        return payload;
    }
}

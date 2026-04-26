package org.example.petstore;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StoreApiTest extends BasePetstoreTest
{
    private static final long ORDER_ID = ThreadLocalRandom.current().nextLong(1L, 10L);
    // Keep PET_ID above Integer.MAX_VALUE so JSON-decoded values are always Long (stable equalTo matching).
    private static final long PET_ID = ThreadLocalRandom.current().nextLong(3_000_000_000L, 9_999_999_999L);

    @Test
    @Order(1)
    void getInventory_returnsStatusCountMap()
    {
        given()
        .when()
                .get("/store/inventory")
        .then()
                .statusCode(200)
                .body("$", notNullValue())
                .body("size()", greaterThanOrEqualTo(1));
    }

    @Test
    @Order(2)
    void placeOrder_returnsCreatedOrder()
    {
        Map<String, Object> body = Map.of(
                "id", ORDER_ID,
                "petId", PET_ID,
                "quantity", 1,
                "shipDate", "2026-04-25T10:15:30.000Z",
                "status", "placed",
                "complete", true);

        given()
                .body(body)
        .when()
                .post("/store/order")
        .then()
                .statusCode(200)
                .body("id", equalTo((int) ORDER_ID))
                .body("petId", equalTo(PET_ID))
                .body("status", equalTo("placed"));
    }

    @Test
    @Order(3)
    void getOrderById_returnsOrder()
    {
        given()
                .pathParam("orderId", ORDER_ID)
        .when()
                .get("/store/order/{orderId}")
        .then()
                .statusCode(200)
                .body("id", equalTo((int) ORDER_ID))
                .body("petId", equalTo(PET_ID));
    }

    @Test
    @Order(4)
    void deleteOrder_returns200()
    {
        given()
                .pathParam("orderId", ORDER_ID)
        .when()
                .delete("/store/order/{orderId}")
        .then()
                .statusCode(200)
                .body("message", equalTo(String.valueOf(ORDER_ID)));
    }

    @Test
    @Order(5)
    void getOrderById_afterDelete_returns404()
    {
        given()
                .pathParam("orderId", ORDER_ID)
        .when()
                .get("/store/order/{orderId}")
        .then()
                .statusCode(404)
                .body("message", equalTo("Order not found"));
    }

    @Test
    @Order(6)
    void getOrderById_outOfRange_returns404()
    {
        given()
                .pathParam("orderId", 99999)
        .when()
                .get("/store/order/{orderId}")
        .then()
                .statusCode(404);
    }
}

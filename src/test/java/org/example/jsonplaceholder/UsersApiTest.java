package org.example.jsonplaceholder;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.Test;

class UsersApiTest extends BaseJsonPlaceholderTest
{
    @Test
    void getAllUsers_returns10Users()
    {
        given()
        .when()
                .get("/users")
        .then()
                .statusCode(200)
                .body("$", hasSize(10))
                .body("id", everyItem(notNullValue()))
                .body("email", everyItem(containsString("@")));
    }

    @Test
    void getUserById_returnsKnownUserWithNestedAddressAndCompany()
    {
        given()
                .pathParam("id", 1)
        .when()
                .get("/users/{id}")
        .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("name", equalTo("Leanne Graham"))
                .body("username", equalTo("Bret"))
                .body("email", containsString("@"))
                .body("address.city", notNullValue())
                .body("address.zipcode", notNullValue())
                .body("address.geo.lat", notNullValue())
                .body("address.geo.lng", notNullValue())
                .body("company.name", notNullValue())
                .body("company.catchPhrase", notNullValue());
    }

    @Test
    void getUserByUsername_returnsSingletonList()
    {
        given()
                .queryParam("username", "Bret")
        .when()
                .get("/users")
        .then()
                .statusCode(200)
                .body("$", hasSize(1))
                .body("[0].id", equalTo(1));
    }

    @Test
    void getUserById_unknownId_returns404()
    {
        given()
                .pathParam("id", 9999)
        .when()
                .get("/users/{id}")
        .then()
                .statusCode(404);
    }
}

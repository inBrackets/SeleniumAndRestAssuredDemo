package org.example.jsonplaceholder;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.Test;

class TodosApiTest extends BaseJsonPlaceholderTest
{
    @Test
    void getAllTodos_returns200Todos()
    {
        given()
        .when()
                .get("/todos")
        .then()
                .statusCode(200)
                .body("$", hasSize(200))
                .body("[0].userId", notNullValue())
                .body("[0].title", notNullValue())
                .body("[0].completed", notNullValue());
    }

    @Test
    void getTodoById_returnsKnownTodo()
    {
        given()
                .pathParam("id", 1)
        .when()
                .get("/todos/{id}")
        .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("userId", equalTo(1))
                .body("title", equalTo("delectus aut autem"))
                .body("completed", equalTo(false));
    }

    @Test
    void getTodosByUserId_filtersByUser()
    {
        given()
                .queryParam("userId", 1)
        .when()
                .get("/todos")
        .then()
                .statusCode(200)
                .body("$", hasSize(20))
                .body("userId", everyItem(equalTo(1)));
    }

    @Test
    void getCompletedTodos_filtersByBooleanFlag()
    {
        given()
                .queryParam("completed", true)
        .when()
                .get("/todos")
        .then()
                .statusCode(200)
                .body("completed", everyItem(equalTo(true)));
    }
}

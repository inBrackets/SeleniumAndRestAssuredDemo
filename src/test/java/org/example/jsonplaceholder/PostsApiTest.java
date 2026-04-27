package org.example.jsonplaceholder;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

import java.util.Map;

import org.junit.jupiter.api.Test;

class PostsApiTest extends BaseJsonPlaceholderTest
{
    @Test
    void getAllPosts_returns100Posts()
    {
        given()
        .when()
                .get("/posts")
        .then()
                .statusCode(200)
                .body("$", hasSize(100))
                .body("[0].id", notNullValue())
                .body("[0].userId", notNullValue())
                .body("[0].title", notNullValue())
                .body("[0].body", notNullValue());
    }

    @Test
    void getPostById_returnsExpectedShape()
    {
        given()
                .pathParam("id", 1)
        .when()
                .get("/posts/{id}")
        .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("userId", equalTo(1))
                .body("title", notNullValue())
                .body("body", notNullValue());
    }

    @Test
    void getPostsByUserId_filtersResults()
    {
        given()
                .queryParam("userId", 1)
        .when()
                .get("/posts")
        .then()
                .statusCode(200)
                .body("$", hasSize(10))
                .body("userId", everyItem(equalTo(1)));
    }

    @Test
    void getCommentsForPost_returnsCommentsScopedToPost()
    {
        given()
                .pathParam("id", 1)
        .when()
                .get("/posts/{id}/comments")
        .then()
                .statusCode(200)
                .body("$", hasSize(5))
                .body("postId", everyItem(equalTo(1)))
                .body("[0].email", notNullValue());
    }

    @Test
    void createPost_returns201WithEchoedBodyAndNewId()
    {
        Map<String, Object> body = Map.of(
                "title", "new title",
                "body", "new body content",
                "userId", 7);

        given()
                .body(body)
        .when()
                .post("/posts")
        .then()
                .statusCode(201)
                .body("id", equalTo(101))
                .body("title", equalTo("new title"))
                .body("body", equalTo("new body content"))
                .body("userId", equalTo(7));
    }

    @Test
    void putPost_returns200WithReplacedDocument()
    {
        Map<String, Object> body = Map.of(
                "id", 1,
                "title", "replaced",
                "body", "replaced body",
                "userId", 1);

        given()
                .pathParam("id", 1)
                .body(body)
        .when()
                .put("/posts/{id}")
        .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("title", equalTo("replaced"))
                .body("body", equalTo("replaced body"));
    }

    @Test
    void patchPost_returns200WithMergedDocument()
    {
        given()
                .pathParam("id", 1)
                .body(Map.of("title", "patched title"))
        .when()
                .patch("/posts/{id}")
        .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("title", equalTo("patched title"))
                .body("body", notNullValue())
                .body("userId", equalTo(1));
    }

    @Test
    void deletePost_returns200()
    {
        given()
                .pathParam("id", 1)
        .when()
                .delete("/posts/{id}")
        .then()
                .statusCode(200);
    }

    @Test
    void getPostById_unknownId_returns404()
    {
        given()
                .pathParam("id", 99999)
        .when()
                .get("/posts/{id}")
        .then()
                .statusCode(404);
    }
}

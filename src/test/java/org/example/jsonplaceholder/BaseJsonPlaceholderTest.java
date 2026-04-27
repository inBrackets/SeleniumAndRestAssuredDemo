package org.example.jsonplaceholder;

import static io.restassured.RestAssured.config;

import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;

public abstract class BaseJsonPlaceholderTest
{
    protected static final String BASE_URI = "https://jsonplaceholder.typicode.com";

    @BeforeAll
    static void setUpRestAssured()
    {
        RestAssured.baseURI = BASE_URI;
        RestAssured.requestSpecification = RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON);
        RestAssured.config = config().httpClient(
                HttpClientConfig.httpClientConfig()
                        .setParam("http.connection.timeout", 10_000)
                        .setParam("http.socket.timeout", 15_000));
    }
}

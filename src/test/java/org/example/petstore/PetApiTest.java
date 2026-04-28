package org.example.petstore;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PetApiTest extends BasePetstoreTest
{
    // Keep IDs above Integer.MAX_VALUE so JSON-decoded values are always Long (stable equalTo matching).
    private static final long PET_ID = ThreadLocalRandom.current().nextLong(3_000_000_000L, 9_999_999_999L);
    private static final String PET_NAME = "Rex-" + PET_ID;

    @Test
    @Order(1)
    void createPet_returns200WithCreatedPet()
    {
        Map<String, Object> body = Map.of(
                "id", PET_ID,
                "name", PET_NAME,
                "status", "available",
                "photoUrls", List.of("https://example.com/rex.jpg"),
                "tags", List.of(Map.of("id", 1, "name", "friendly")),
                "category", Map.of("id", 1, "name", "Dogs"));

        given()
                .body(body)
        .when()
                .post("/pet")
        .then()
                .statusCode(200)
                .body("id", equalTo(PET_ID))
                .body("name", equalTo(PET_NAME))
                .body("status", equalTo("available"));
    }

    @Test
    @Order(2)
    void getPetById_returnsCreatedPet()
    {
        given()
                .pathParam("petId", PET_ID)
        .when()
                .get("/pet/{petId}")
        .then()
                .statusCode(200)
                .body("id", equalTo(PET_ID))
                .body("name", equalTo(PET_NAME))
                .body("photoUrls", hasSize(greaterThanOrEqualTo(1)));
    }

    @Test
    @Order(3)
    void updatePet_changesStatusToSold()
    {
        Map<String, Object> body = Map.of(
                "id", PET_ID,
                "name", PET_NAME,
                "status", "sold",
                "photoUrls", List.of("https://example.com/rex.jpg"));

        given()
                .body(body)
        .when()
                .put("/pet")
        .then()
                .statusCode(200)
                .body("status", equalTo("sold"));
    }

    @Test
    @Order(4)
    void findByStatus_returnsListContainingExpectedStatus()
    {
        given()
                .queryParam("status", "available")
        .when()
                .get("/pet/findByStatus")
        .then()
                .statusCode(200)
                .body("status", everyItem(equalTo("available")));
    }

    @Test
    @Order(5)
    void findByTags_returnsList()
    {
        given()
                .queryParam("tags", "friendly")
        .when()
                .get("/pet/findByTags")
        .then()
                .statusCode(200)
                .body("$", notNullValue());
    }

    @Test
    @Order(6)
    void updatePetWithFormData_acceptsFormFields()
    {
        given()
                .contentType(ContentType.URLENC)
                .pathParam("petId", PET_ID)
                .formParam("name", PET_NAME + "-renamed")
                .formParam("status", "pending")
        .when()
                .post("/pet/{petId}")
        .then()
                .statusCode(200);
    }

    @Test
    @Order(7)
    void uploadImage_returns200WithUploadMessage()
    {
        byte[] png = readPetPng();

        given()
                .contentType("multipart/form-data")
                .pathParam("petId", PET_ID)
                .multiPart("file", "pet.png", png, "image/png")
                .multiPart("additionalMetadata", "rest-assured upload")
        .when()
                .post("/pet/{petId}/uploadImage")
        .then()
                .statusCode(200)
                .body("code", equalTo(200))
                .body("message", containsString("File uploaded"))
                .body("message", containsString(png.length + " bytes"));
    }

    @Test
    @Order(8)
    void deletePet_returns200()
    {
        given()
                .pathParam("petId", PET_ID)
        .when()
                .delete("/pet/{petId}")
        .then()
                .statusCode(200)
                .body("message", equalTo(String.valueOf(PET_ID)));
    }

    @Test
    @Order(9)
    void getPetById_afterDelete_returns404()
    {
        given()
                .pathParam("petId", PET_ID)
        .when()
                .get("/pet/{petId}")
        .then()
                .statusCode(404)
                .body("message", equalTo("Pet not found"));
    }

    @Test
    @Order(10)
    void getPetById_invalidId_returns404()
    {
        given()
                .pathParam("petId", "not-a-number")
        .when()
                .get("/pet/{petId}")
        .then()
                .statusCode(404);
    }

    @Test
    @Order(11)
    void findByStatus_withMultipleValues_returnsCombinedResults()
    {
        given()
                .queryParam("status", "available", "pending")
        .when()
                .get("/pet/findByStatus")
        .then()
                .statusCode(200)
                .body("status", hasItem(equalTo("available")));
    }

    @Test
    @Order(12)
    void findByTags_returnsListOfPetsWithNonBlankName()
    {
        List<Pet> pets = given()
                .queryParam("tags", "friendly")
        .when()
                .get("/pet/findByTags")
        .then()
                .statusCode(200)
                .extract()
                .as(new TypeRef<List<Pet>>() {});

        assertThat(pets)
                .isNotEmpty()
                .allSatisfy(pet -> assertThat(pet.getName()).isNotBlank());
    }

    private static byte[] readPetPng()
    {
        try (InputStream in = PetApiTest.class.getResourceAsStream("/pet.png")) {
            Objects.requireNonNull(in, "pet.png not found on test classpath");
            return in.readAllBytes();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read pet.png", e);
        }
    }
}

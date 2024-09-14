package com.example.eztask;


import com.example.eztask.enums.ResponseCode;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.emptyArray;
import static org.hamcrest.Matchers.equalTo;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FreelancerControllerE2ETest {

    private static final Logger log = LoggerFactory.getLogger(FreelancerControllerE2ETest.class);

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        RestAssured.basePath = "/api/freelancer";
    }

    @Test
    @DisplayName("프리랜서 생성 E2E 테스트, 생성 후 단일조회")
    void testCreateAndGetFreelancerProfile() {
        // 프리랜서 생성
        long id = given()
            .contentType("application/json")
            .when()
            .post("/create")
            .then()
            .statusCode(200)
            .body("code", equalTo(ResponseCode.SUCCESS.getCode()))
            .body("message", equalTo(ResponseCode.SUCCESS.getMessage()))
            .body("data", is(notNullValue()))
            .extract().jsonPath().getLong("data");

        // 생성된 프리랜서 조회
        given()
            .contentType("application/json")
            .when()
            .get("/profiles/" + id)
            .then()
            .statusCode(200)
            .body("code", equalTo(ResponseCode.SUCCESS.getCode()))
            .body("message", equalTo(ResponseCode.SUCCESS.getMessage()))
            .body("data", is(notNullValue()))
            .body("data.id", equalTo((int) id));
    }

    @Test
    @DisplayName("프리랜서 리스트 조회 E2E 테스트")
    void testGetFreelancerProfileList() {
        given()
            .contentType("application/json")
            .when()
            .get()
            .then()
            .statusCode(200)
            .body("code", equalTo(ResponseCode.SUCCESS.getCode()))
            .body("message", equalTo(ResponseCode.SUCCESS.getMessage()))
            .body("data.content", is(not(emptyArray())))
            .body("data.content.size()", greaterThan(0));
    }

    @Test
    @DisplayName("존재하지 않는 프리랜서 조회 E2E 테스트")
    void testGetFreelancerProfileNotFound() {
        given()
            .contentType("application/json")
            .when()
            .get("/profiles/999999")
            .then()
            .statusCode(404);
    }
}

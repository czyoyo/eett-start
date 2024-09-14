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
import io.restassured.response.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.emptyArray;
import static org.hamcrest.Matchers.equalTo;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FreelancerControllerE2ETest {

    private static final Logger log = LoggerFactory.getLogger(FreelancerControllerE2ETest.class);
    @LocalServerPort
    private int port;  // Spring이 실행 중인 포트를 동적으로 할당

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port; // 서버 포트 설정
    }

    @Test
    @DisplayName("프리랜서 생성 E2E 테스트, 생성 후 단일조회")
    void testCreateFreelancerProfile() {
        // API 호출
        Response response = given()
            .contentType("application/json")
            .when()
            .post("/api/freelancer/create")
            .then()
            .statusCode(200)
            .body("code", equalTo(ResponseCode.SUCCESS.getCode()))
            .body("message", equalTo(ResponseCode.SUCCESS.getMessage()))
            .body("data", is(notNullValue()))
            .extract().response();

        Object data = response.jsonPath().get("data");
        // data 가 1 이다 Long 타입으로 변환
        long id = Long.parseLong(data.toString());

        // API 호출
        Response singleResponse = given()
            .contentType("application/json")
            .when()
            .get("/api/freelancer/profiles/" + id)
            .then()
            .statusCode(200)
            .body("code", equalTo(ResponseCode.SUCCESS.getCode()))
            .body("message", equalTo(ResponseCode.SUCCESS.getMessage()))
            .body("data", is(notNullValue()))
            .extract().response();

        assertThat(singleResponse.jsonPath().get().toString()).isNotEmpty();
    }

    @Test
    @DisplayName("프리랜서 리스트 조회 E2E 테스트")
    void testGetFreelancerProfileList() {
        // API 호출
        Response response = given()
            .contentType("application/json")
            .when()
            .get("/api/freelancer")
            .then()
            .statusCode(200)
            .body("code", equalTo(ResponseCode.SUCCESS.getCode()))
            .body("message", equalTo(ResponseCode.SUCCESS.getMessage()))
            .body("data.content", is(not(emptyArray())))
            .extract().response();

        assertThat(response.jsonPath().getList("data.content").size()).isGreaterThan(0);
    }

    @Test
    @DisplayName("존재하지 않는 프리랜서 조회 E2E 테스트")
    void testGetFreelancerProfileNotFound() {
        // API 호출
        Response response = given()
            .contentType("application/json")
            .when()
            .get("/api/freelancer/profiles/999999")
            .then()
            .statusCode(404)
            .extract().response();
    }

}

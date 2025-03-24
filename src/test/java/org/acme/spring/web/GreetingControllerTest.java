package org.acme.spring.web;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@DisabledOnOs(OS.WINDOWS)
@QuarkusTest
public class GreetingControllerTest {

    @Test
    public void testHelloEndpoint() {
        given().when().get("/greeting").then().statusCode(200).body(is("{\"message\":\"Hola world\"}"));
    }

}

package io.springframework.contracts;

import io.restassured.RestAssured;
import io.springframework.flights.FlightServiceApp;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = FlightServiceApp.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
                "eureka.client.register-with-eureka=false",
                "eureka.client.fetch-registry=false",
                "spring.cloud.config.enabled=false",
                "server.servlet.context-path=/flights"
        })
public abstract class FlightsBaseTest {

    @LocalServerPort
    int port;

    @Before
    public void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = this.port;
    }

}

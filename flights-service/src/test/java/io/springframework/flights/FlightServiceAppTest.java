package io.springframework.flights;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestPropertySource(properties = {
                "eureka.client.register-with-eureka=false",
                "eureka.client.fetch-registry=false",
                "spring.cloud.config.enabled=false"
        })
public class FlightServiceAppTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    public void contextLoads() {
    }

    @Test
    public void get_flight_should_return_successfully() {
        Flight flight =
                restTemplate.getForObject("/1", Flight.class);

        Assertions.assertThat(flight).isNotNull();
        Assertions.assertThat(flight.getId()).isGreaterThan(0);
        Assertions.assertThat(flight.getAirline()).isNotBlank();
        Assertions.assertThat(flight.getPlane()).isNotBlank();
        Assertions.assertThat(flight.getOrigin()).isNotBlank();
        Assertions.assertThat(flight.getDestination()).isNotBlank();
    }

}

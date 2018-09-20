package io.agilehandy.reservation;


import io.agilehandy.reservation.flight.Flight;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.net.URI;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureStubRunner(stubsMode = StubRunnerProperties.StubsMode.LOCAL,
 ids = "io.agilehandy:flights-service:+:stubs:8090")
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@DirtiesContext
@TestPropertySource(properties = {
        "stubrunner.idsToServiceIds.flights-service=flights-service",
        "spring.cloud.config.enabled=false",
        "eureka.client.register-with-eureka=false",
        "eureka.client.fetch-registry=true"
  })
public class ReservationsIntegrationTest {


    TestRestTemplate restTemplate;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ReservationController controller;


    public void test_search_flights() {
        RequestEntity request = new RequestEntity(HttpMethod.resolve("GET")
                        , URI.create("/search/AUS/IAH/2018-05-05"));
        ResponseEntity<List<Flight>> response =
                restTemplate.exchange(request, new ParameterizedTypeReference<List<Flight>>() {});

        List<Flight> flights = response.getBody();
        int status = response.getStatusCodeValue();

        Assertions.assertThat(flights).isNotNull();
        Assertions.assertThat(flights.size()).isGreaterThan(0);
    }

    @Test
    public void search_flights_should_run_successfully() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/search/AUS/IAH/2018-05-05"))
                .andExpect(status().isOk())
                ;
    }
}

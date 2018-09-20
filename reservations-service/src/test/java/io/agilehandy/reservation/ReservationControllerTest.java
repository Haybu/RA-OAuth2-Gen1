package io.agilehandy.reservation;

import io.agilehandy.reservation.exceptions.ReservationException;
import io.agilehandy.reservation.flight.Flight;
import io.agilehandy.security.SecurityTestConfig;
import io.agilehandy.security.WithOAuthSubject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Date;

import static com.google.common.net.HttpHeaders.AUTHORIZATION;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ReservationController.class)
@Import(SecurityTestConfig.class)
@TestPropertySource(properties = {
        "spring.cloud.config.enabled=false",
        "eureka.client.register-with-eureka=false",
        "eureka.client.fetch-registry=false"
})
public class ReservationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ReservationService reservationService;

    @MockBean
    public ResourceServerTokenServices resourceServerTokenServices;

    @Before
    public void setup() {
        // Stub the remote call that loads the authentication object
        when(resourceServerTokenServices.loadAuthentication(anyString()))
                .thenAnswer(invocation -> SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    @WithOAuthSubject
    public void getFlight_shouldReturnFlight() throws Exception {
        Date arrivalDate = new Date();
        Date departureDate = new Date();

        Flight flight = new Flight();
        flight.setAirline("Delta");
        flight.setCapacity(150);
        flight.setOrigin("X");
        flight.setDestination("Y");
        flight.setNbr("FL000");
        flight.setDeparture(departureDate);
        flight.setArrival(arrivalDate);

        given(reservationService.searchDatedFlights(anyString(), anyString(), any(), any()))
                .willReturn(Arrays.asList(flight));

        mockMvc.perform(MockMvcRequestBuilders.get("/search/from/to/2018-05-05")
                .header(AUTHORIZATION, "Bearer foo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].airline").value("Delta"))
                .andExpect(jsonPath("$.[0].capacity").value(150))
                .andExpect(jsonPath("$.[0].origin").value("X"))
                .andExpect(jsonPath("$.[0].destination").value("Y"))
                ;
    }

    @Test
    @WithOAuthSubject
    public void getFlight_shouldReturnException() throws Exception {
        given(reservationService.searchDatedFlights(anyString(), anyString(), any(), any()))
                .willThrow(new ReservationException("Flight Not Found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/search/from/to/2018-05-05")
                .header(AUTHORIZATION, "Bearer foo"))
                .andExpect(status().isNotFound());
    }

}
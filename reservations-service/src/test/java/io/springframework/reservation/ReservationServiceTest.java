package io.springframework.reservation;

import io.springframework.reservation.entities.Address;
import io.springframework.reservation.entities.Passenger;
import io.springframework.reservation.entities.Reservation;
import io.springframework.reservation.flight.Flight;
import io.springframework.reservation.flight.FlightClient;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

@RunWith(MockitoJUnitRunner.class)
public class ReservationServiceTest {

    @Mock
    private ReservationRepository repository;

    @Mock
    private FlightClient client;

    private ReservationService service;

    private Flight flight;

    private final Date arrivalDate = new Date();
    private final Date departureDate = new Date();

    @Before
    public void setup() throws Exception {
        service = new ReservationService(client, repository);

        flight = new Flight();
        flight.setAirline("Delta");
        flight.setCapacity(150);
        flight.setOrigin("X");
        flight.setDestination("Y");
        flight.setNbr("FL000");
        flight.setDeparture(departureDate);
        flight.setArrival(arrivalDate);
    }

    @Test
    public void searchDatedFlightsTest() throws Exception {
        given(client.findDatedFlights(anyString(), anyString(), any(), any()))
                .willReturn(Arrays.asList(flight));

        List<Flight> flights =
                service.searchDatedFlights(anyString(), anyString(), any(), any());

        assertFlights(flights);
    }

    @Test
    public void searchFlightsTest() throws Exception {
        given(client.findFlights(anyString(), anyString()))
                .willReturn(Arrays.asList(flight));

        List<Flight> flights =
                service.searchFlights(anyString(), anyString());

        assertFlights(flights);
    }

    @Test
    public void reserveSeatTest_allowed() {
        willDoNothing().given(client).update(any());
        given(client.findById(anyLong())).willReturn(flight);

        boolean result = service.reserveSeats(anyLong(), 1);
        Assertions.assertThat(result).isEqualTo(true);
    }

    @Test
    public void reserveSeatTest_notAllowed() {
        flight.setCapacity(3);
        given(client.findById(anyLong())).willReturn(flight);

        boolean result = service.reserveSeats(anyLong(), 5);
        Assertions.assertThat(result).isEqualTo(false);
    }

    @Test(expected = InvocationTargetException.class)
    public void bookFlight_ExceptionTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        given(client.findById(anyLong())).willReturn(null);

        Method method = service.getClass()
                .getDeclaredMethod("bookFlight", Long.class, List.class, Address.class);

        method.setAccessible(true);

        method.invoke(service, 1L, Arrays.asList(new Passenger()), new Address());
    }

    @Test
    public void bookFlight_Test() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        given(client.findById(anyLong())).willReturn(flight);

        Reservation reservation = new Reservation();
        reservation.setConfirmationNumber(UUID.randomUUID().toString());

        given(repository.save(any())).willReturn(reservation);

        Method method = service.getClass()
                .getDeclaredMethod("bookFlight", Long.class, List.class, Address.class);

        method.setAccessible(true);

        Reservation reservation2 =
                (Reservation) method.invoke(service, 1L, Arrays.asList(new Passenger()), new Address());

        Assertions.assertThat(reservation2.getConfirmationNumber()).isNotEmpty();
    }

    private void assertFlights(List<Flight> flights) {
        Assertions.assertThat(flights.size()).isEqualTo(1);
        Assertions.assertThat(flights.get(0).getAirline()).isEqualTo("Delta");
        Assertions.assertThat(flights.get(0).getCapacity()).isEqualTo(150);
        Assertions.assertThat(flights.get(0).getOrigin()).isEqualTo("X");
        Assertions.assertThat(flights.get(0).getDestination()).isEqualTo("Y");
        Assertions.assertThat(flights.get(0).getDeparture()).isEqualTo(departureDate);
        Assertions.assertThat(flights.get(0).getArrival()).isEqualTo(arrivalDate);
    }


}
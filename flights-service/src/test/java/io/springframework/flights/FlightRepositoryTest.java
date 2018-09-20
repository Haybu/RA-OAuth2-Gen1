package io.springframework.flights;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@DataJpaTest()
@TestPropertySource(properties = {
                "eureka.client.register-with-eureka=false",
                "eureka.client.fetch-registry=false",
                "spring.cloud.config.enabled=false"
        })
public class FlightRepositoryTest {

    @Autowired
    private FlightRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    Flight flight;
    Date departureDate = new Date();
    Date arrivalDate = new Date();

    @Before
    public void setup() {
        flight = new Flight();
        flight.setAirline("American");
        flight.setCapacity(200);
        flight.setOrigin("X");
        flight.setDestination("Y");
        flight.setNbr("FL000");
        flight.setDeparture(departureDate);
        flight.setArrival(arrivalDate);
    }

    @Test
    public void findById_returnFlight() {
        Long id = (Long) entityManager.persistAndGetId(flight);

        Flight persistedFlight = repository.findById(id).get();

        Assertions.assertThat(persistedFlight.getOrigin()).isEqualTo("X");
        Assertions.assertThat(persistedFlight.getDestination()).isEqualTo("Y");
        Assertions.assertThat(persistedFlight.getNbr()).isEqualTo("FL000");
        Assertions.assertThat(persistedFlight.getOrigin()).isEqualTo("X");
        Assertions.assertThat(persistedFlight.getDeparture()).isEqualTo(departureDate);
        Assertions.assertThat(persistedFlight.getArrival()).isEqualTo(arrivalDate);
    }

    @Test
    public void update_should_run_successfully() {
        flight.setCapacity(100);
        Flight f = entityManager.persist(flight);
        Assertions.assertThat(f.getId()).isEqualTo(flight.getId());
        Assertions.assertThat(f.getCapacity()).isEqualTo(100);
    }

}
package io.agilehandy.reservation;

import io.agilehandy.reservation.entities.Reservation;
import org.springframework.data.repository.CrudRepository;

public interface ReservationRepository extends CrudRepository<Reservation, Long> {

}

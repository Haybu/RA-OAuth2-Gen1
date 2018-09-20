package io.springframework.reservation;

import io.springframework.reservation.entities.Reservation;
import org.springframework.data.repository.CrudRepository;

public interface ReservationRepository extends CrudRepository<Reservation, Long> {

}

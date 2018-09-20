/*
 * Copyright 2012-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.springframework.reservation;

import io.springframework.reservation.entities.Address;
import io.springframework.reservation.entities.Passenger;
import io.springframework.reservation.entities.Reservation;
import io.springframework.reservation.entities.ReservationRequest;
import io.springframework.reservation.exceptions.ReservationException;
import io.springframework.reservation.flight.Flight;
import io.springframework.reservation.flight.FlightClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Haytham Mohamed
 */

@Service
@Slf4j
public class ReservationService {

	private final FlightClient flightClient;

	private final ReservationRepository reservationRepository;

	public ReservationService(FlightClient flightClient,
							  ReservationRepository reservationRepository) {
		this.flightClient = flightClient;
		this.reservationRepository = reservationRepository;
	}

	// @HystrixCommand(fallbackMethod = "reliableBooking")
	public String book(ReservationRequest reservationRequest) {
		log.info("reserving in flight " + reservationRequest.getFlightId());

		Reservation reservation = this.bookFlight(reservationRequest.getFlightId(),
				reservationRequest.getPassengers(), reservationRequest.getAddress());

		return reservation.getConfirmationNumber();
	}

	private Reservation bookFlight(Long flightId, List<Passenger> passengers, Address address) {
		// fetch the flight again in case changes happens
		Flight flight = this.findById(flightId);
		if (flight == null) {
			log.error("No flights found!");
			throw new ReservationException("Selected flight is NOT found!");

		}

		boolean reserve = this.reserveSeats(flightId, passengers.size());
		Reservation reservation = new Reservation();

		if (reserve) {
			log.info("about to reserve the flight...");

			passengers.stream().forEach(p -> p.setAddress(address));

			reservation.setOrigin(flight.getOrigin());
			reservation.setDestination(flight.getDestination());
			reservation.setFlightId(flightId);
			reservation.setConfirmationNumber(UUID.randomUUID().toString());
			reservation.setFlightNumber(flight.getNbr());
			reservation.setPassengers(passengers);
			reservation.setDeparture(flight.getDeparture().toInstant()
					.atZone(ZoneId.systemDefault()).toLocalDate());
			reservation.setArrival(flight.getArrival().toInstant()
					.atZone(ZoneId.systemDefault()).toLocalDate());

			Reservation saved_reservation = reservationRepository.save(reservation);
			log.info("reservation is successful.");
			return saved_reservation;
		}

		log.info("cannot reserve in flight id= " + flightId);
		return reservation;
	}

	public String reliableBooking(ReservationRequest reservationRequest) {
		log.info(
				"Something went wrong. Circuit breaker opens an a temporary booking is generated");
		return "No Confirmation Number Generated";
	}

	public List<Flight> searchDatedFlights(String from, String to, Date minDate,
										   Date maxDate) throws ParseException {
		log.info("searching flights from " + from + " to " + to + " between " + minDate
				+ " and " + maxDate);
		return flightClient.findDatedFlights(from, to, minDate, maxDate);
	}

	public List<Flight> searchFlights(String from, String to) {
		log.info("searching flights from " + from + " to " + to);
		return flightClient.findFlights(from, to);
	}

	public boolean reserveSeats(Long flightId, int count) {
		Flight flight = this.findById(flightId);
		int capacity = flight.getCapacity();
		if (capacity - count >= 0) {
			flight.setCapacity(capacity - count);
			flightClient.update(flight);
			return true;
		}
		return false;
	}

	public List<String> getFlightOrigins() {
		return flightClient.origins();
	}

	public List<String> getFlightDestinations() {
		return flightClient.destinations();
	}

	private Flight findById(Long flightId) {
		return flightClient.findById(flightId);
	}

}

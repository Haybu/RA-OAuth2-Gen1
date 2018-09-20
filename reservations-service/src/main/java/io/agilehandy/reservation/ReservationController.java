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

package io.agilehandy.reservation;

import io.agilehandy.reservation.entities.ReservationRequest;
import io.agilehandy.reservation.exceptions.ExceptionMessage;
import io.agilehandy.reservation.exceptions.ReservationException;
import io.agilehandy.reservation.flight.Flight;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Haytham Mohamed
 */

@RestController
@Slf4j
public class ReservationController {

	private final ReservationService reservationService;

	public ReservationController(ReservationService reservationService) {
		this.reservationService = reservationService;
	}

	@GetMapping("/search/{from}/{to}")
	public List<Flight> searchFlights(@PathVariable String from, @PathVariable String to) {
		return reservationService.searchFlights(from, to);

	}

	@GetMapping("/search/{from}/{to}/{date}")
	public List<Flight> searchDatedFlights(@PathVariable String from,
										   @PathVariable String to,
										   @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date date)
			throws ParseException {
		LocalDate dt = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate mindt = dt.minusDays(3);
		LocalDate maxdt = dt.plusDays(3);
		java.util.Date mindtdt = java.sql.Date.valueOf(mindt);
		java.util.Date maxdtdt = java.sql.Date.valueOf(maxdt);
		return reservationService.searchDatedFlights(from, to, mindtdt, maxdtdt);
	}

	@PostMapping("/book")
	public ReservationRequest book(@RequestBody ReservationRequest reservationRequest) {
		log.info("Booking a flight for " + reservationRequest.getPassengers());
		String confirmation = reservationService.book(reservationRequest);
		reservationRequest.setConfirmation(confirmation);
		return reservationRequest;
	}

	@GetMapping("/search/origins")
	public List<String> origins() {
		return this.reservationService.getFlightOrigins();
	}

	@GetMapping("/search/destinations")
	public List<String> destinations() {
		return this.reservationService.getFlightDestinations();
	}

	@ExceptionHandler(ReservationException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ExceptionMessage handleException(ReservationException exception) {
		return new ExceptionMessage(exception.getExceptionMsg(),
				exception.getCause() == null ? "" : exception.getCause().getMessage(),
				Arrays.stream(exception.getStackTrace())
						.map(sequence -> String.valueOf(sequence))
						.collect(Collectors.joining("\n")));
	}

}

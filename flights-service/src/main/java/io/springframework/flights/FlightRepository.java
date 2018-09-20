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

package io.springframework.flights;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @author Haytham Mohamed
 */

public interface FlightRepository extends PagingAndSortingRepository<Flight, Long> {

	@Query("SELECT f FROM Flight f WHERE f.origin = ?1 AND f.destination = ?2 "
			+ " AND f.departure between ?3 and ?4")
	public List<Flight> findFlightsByCustomQueryDated(@Param("origin") String origin,
			@Param("destination") String destination,
			@Param("mindate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date mindate,
			@Param("maxdate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date maxdate);

	public List<Flight> findByOriginAndDestination(@Param("origin") String origin,
			@Param("destination") String destination);

}

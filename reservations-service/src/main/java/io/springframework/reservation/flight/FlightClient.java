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

package io.springframework.reservation.flight;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author Haytham Mohamed
 */

@FeignClient(name = "flights-service")
public interface FlightClient {

	@RequestMapping(method = RequestMethod.GET, value = "/flights/datedlegs")
	List<Flight> findDatedFlights(@RequestParam("origin") String origin,
								  @RequestParam("destination") String destination,
								  @RequestParam("mindate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date mindate,
								  @RequestParam("maxdate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date maxdate);

	@RequestMapping(method = RequestMethod.GET, value = "/flights/legs")
	List<Flight> findFlights(@RequestParam("origin") String origin,
							 @RequestParam("destination") String destination);

	@RequestMapping(method = RequestMethod.GET, value = "/flights/{id}")
	Flight findById(@PathVariable("id") Long id);

	@RequestMapping(method = RequestMethod.PUT, value = "/flights")
	void update(@RequestBody Flight flight);

	@RequestMapping(method = RequestMethod.GET, value = "/flights/origins")
	List<String> origins();

	@RequestMapping(method = RequestMethod.GET, value = "/flights/destinations")
	List<String> destinations();

}

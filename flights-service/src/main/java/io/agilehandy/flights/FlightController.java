package io.agilehandy.flights;


import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
public class FlightController {

    private final FlightRepository repository;

    public FlightController(FlightRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/datedlegs")
    public List<Flight> getDatedFlightLegs(@RequestParam String origin, @RequestParam String destination,
                                           @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date mindate,
                                           @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date maxdate) {
        return repository.findFlightsByCustomQueryDated(origin, destination, mindate, maxdate);
    }

    @GetMapping("/legs")
    public List<Flight> getFlightLegs(@RequestParam String origin, @RequestParam String destination) {
        return repository.findByOriginAndDestination(origin, destination);
    }

    @GetMapping("/{id}")
    public Flight getFlightById(@PathVariable("id") Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new FlightNotFoundException("No flight with id " + id));
    }

    @PutMapping
    public Flight putFlightById(@RequestBody Flight flight) {
        return repository.save(flight);
    }
}


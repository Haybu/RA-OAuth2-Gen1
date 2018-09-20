package io.agilehandy.reservation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.hateoas.config.EnableHypermediaSupport;

@SpringBootApplication
@EnableFeignClients
@EnableCircuitBreaker
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class ReservationServiceApp {

	public static void main(String[] args) {
		SpringApplication.run(ReservationServiceApp.class, args);
	}

}

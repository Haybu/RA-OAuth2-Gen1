package io.springframework.reservation.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Address {

	@Id
	@GeneratedValue
	private Long id;

	private String address1;

	private String address2;

	@Column(name = "zip_code")
	private String zipCode;

	private String state;

	private String country;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "address", cascade = {
			CascadeType.REFRESH })
	private Passenger passenger;

}

package io.agilehandy.reservation.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Passenger {

	public static enum Gender {

		MALE, FEMALE

	}

	@Id
	@GeneratedValue
	private Long id;

	@NotNull
	@Column(name = "first_name")
	private String firstName;

	@NotNull
	@Column(name = "last_name")
	private String lastName;

	private String email;

	@Column(name = "dob")
	private LocalDate dateOfBirth;

	private int age;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Gender gender;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "address_id")
	private Address address;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH })
	@JoinColumn(name = "reservation_id")
	private Reservation reservation;

}

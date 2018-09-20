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

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Haytham Mohamed
 */

@Entity
@Data
@NoArgsConstructor
@ToString
public class Flight {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String nbr;

	private String airline;

	private String origin;

	private String destination;

	private Integer stops;

	private Double price;

	private Integer capacity;

	private String plane;

	@Temporal(TemporalType.DATE)
	private Date departure;

	@Temporal(TemporalType.DATE)
	private Date arrival;

}

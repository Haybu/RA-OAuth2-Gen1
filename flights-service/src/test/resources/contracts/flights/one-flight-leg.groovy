package contracts

org.springframework.cloud.contract.spec.Contract.make {
    description 'should return one flight leg'
    name 'one_flight_legs'
    request {
        method 'GET'
        urlPath ('/flights/1')
    }
    response {
        status OK()
        body(file("one-flight-response.json"))
        headers {
            contentType(applicationJson())
        }
    }
}
package contracts

org.springframework.cloud.contract.spec.Contract.make {
    description 'should return flight legs'
    name 'flight_legs'
    request {
        method 'GET'
        url ('/flights/legs') {
            queryParameters {
                parameter 'origin': value(consumer(regex('\\w+')), producer('AUS'))
                parameter 'destination': value(consumer(regex('\\w+')), producer('IAH'))
            }
        }
    }
    response {
        status OK()
        body(file("aus-iah-legs-response.json"))
        headers {
            contentType(applicationJson())
        }
    }
}
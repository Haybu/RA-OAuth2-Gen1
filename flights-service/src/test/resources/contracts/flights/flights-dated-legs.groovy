package contracts

org.springframework.cloud.contract.spec.Contract.make {
    description 'should return dated flight legs'
    name 'dated_flight_legs'
    request {
        method 'GET'
        url ('/flights/datedlegs') {
            queryParameters {
                parameter 'origin': value(consumer(regex('\\w+')), producer('AUS'))
                parameter 'destination': value(consumer(regex('\\w+')), producer('IAH'))
                parameter 'mindate': value(consumer(regex('\\d{4}-\\d{2}-\\d{2}')), producer('2018-05-02'))
                parameter 'maxdate': value(consumer(regex('\\d{4}-\\d{2}-\\d{2}')), producer('2018-05-08'))
            }
        }
    }
    response {
        status OK()
        body(file("multiple-flights-response.json"))
        headers {
            contentType(applicationJson())
        }
    }
}
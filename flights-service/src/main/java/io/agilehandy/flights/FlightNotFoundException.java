package io.agilehandy.flights;

public class FlightNotFoundException extends RuntimeException {

    private String exceptionMsg;

    public FlightNotFoundException(String exceptionMsg) {
        this.exceptionMsg = exceptionMsg;
    }

    public String getExceptionMsg() {
        return this.exceptionMsg;
    }

    public void setExceptionMsg(String exceptionMsg) {
        this.exceptionMsg = exceptionMsg;
    }

}

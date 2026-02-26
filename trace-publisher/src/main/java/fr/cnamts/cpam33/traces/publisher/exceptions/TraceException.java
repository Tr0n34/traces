package fr.cnamts.cpam33.traces.publisher.exceptions;

public class TraceException extends RuntimeException {

    private final ExceptionCode exceptionCode;
    private final String message;

    public TraceException(ExceptionCode exceptionCode, String message) {
        this.exceptionCode = exceptionCode;
        this.message = message;
    }

    public ExceptionCode exceptionCode() {
        return exceptionCode;
    }

    public String message() {
        return message;
    }

}

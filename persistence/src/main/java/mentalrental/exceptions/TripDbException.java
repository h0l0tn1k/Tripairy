package mentalrental.exceptions;

public class TripDbException extends RuntimeException {

    public TripDbException() {
    }

    public TripDbException(String message) {
        super(message);
    }

    public TripDbException(String message, Throwable cause) {
        super(message, cause);
    }
}

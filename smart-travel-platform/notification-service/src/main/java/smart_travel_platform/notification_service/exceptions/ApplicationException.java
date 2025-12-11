package smart_travel_platform.notification_service.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApplicationException extends Exception {
    private final HttpStatus httpStatusCode;
    public ApplicationException( HttpStatus httpStatusCode,String message) {
        super(message);
        this.httpStatusCode = httpStatusCode;
    }
}

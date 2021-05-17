package port_simulation.service2;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class IncorrectFilenameException extends Exception{
    public IncorrectFilenameException(String message) {
        super(message);
    }
}

package pl.gorzki.fmspring.commons;

import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static java.util.Collections.emptyList;


@Value
public class AppResponse {
    public static AppResponse SUCCESS = new AppResponse(true, emptyList());
    boolean success;
    List<String> errors;

    public void checkResponseSuccess() {
        if (!success) {
            String message = String.join(", ", errors);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }

    }
}

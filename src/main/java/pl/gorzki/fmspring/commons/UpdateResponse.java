package pl.gorzki.fmspring.commons;

import lombok.Value;

import java.util.List;

import static java.util.Collections.emptyList;

@Value
public class UpdateResponse {
    public static UpdateResponse SUCCESS = new UpdateResponse(true, emptyList());
    boolean success;
    List<String> errors;
}

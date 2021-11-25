package pl.gorzki.fmspring.commons;

import java.util.List;

import static java.util.Collections.emptyList;


public record UpdateResponse(boolean success, List<String> errors) {
    public static UpdateResponse SUCCESS = new UpdateResponse(true, emptyList());
}

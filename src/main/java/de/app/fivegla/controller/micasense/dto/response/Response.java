package de.app.fivegla.controller.micasense.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * Base class for all responses.
 */
@Getter
abstract class Response {

    /**
     * The timestamp of the response.
     */
    @Schema(description = "The timestamp of the response.")
    private final String timestamp;

    public Response() {
        timestamp = DateTimeFormatter.ISO_INSTANT.format(Instant.now());
    }
}

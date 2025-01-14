package de.app.fivegla.controller.dto.response.inner;

import de.app.fivegla.api.Manufacturer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Builder
@Schema(name = "Response for finding all third API responses.")
public class ThirdPartyApiConfiguration {

    /**
     * Represents the tenant.
     */
    @Schema(description = "The tenant ID.")
    private String tenantId;

    /**
     * Represents the manufacturer.
     */
    @Schema(description = "The manufacturer.")
    private Manufacturer manufacturer;

    /**
     * Represents the prefix used for FIWARE integration.
     */
    @Schema(description = "The FIWARE prefix.")
    private String fiwarePrefix;

    /**
     * Indicator if the configuration is enabled.
     */
    @Schema(description = "The enabled status.")
    private boolean enabled;

    /**
     * Represents the URL of the third-party API.
     */
    @Schema(description = "The URL of the third-party API.")
    private String url;

    /**
     * Represents the UUID of the third-party API. Will be set automatically to a random UUID.
     */
    @Schema(description = "The UUID of the third-party API.")
    private String uuid;

    /**
     * Represents the last run of the third-party API.
     */
    @Schema(description = "The last run of the third-party API.")
    private Instant lastRun;
}

package de.app.fivegla.persistence.entity;

import de.app.fivegla.controller.dto.request.CreateGroupRequest;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/**
 * Represents a group of sensors or devices, created by the tenant.
 */
@Getter
@Setter
public class Group {

    /**
     * The id of the group. Should be unique within the tenant.
     */
    private String groupId;

    /**
     * The tenant of the group.
     */
    private Tenant tenant;

    /**
     * The name of the group.
     */
    private String name;

    /**
     * The description of the group.
     */
    private String description;

    /**
     * The creation date of the group.
     */
    private Instant createdAt;

    /**
     * The last update date of the group.
     */
    private Instant updatedAt;

    public static Group from(CreateGroupRequest createGroupRequest) {
        Group group = new Group();
        group.setName(createGroupRequest.getName());
        group.setDescription(createGroupRequest.getDescription());
        return group;
    }
}

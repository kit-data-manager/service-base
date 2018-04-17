/*
 * Copyright 2017 Karlsruhe Institute of Technology.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.kit.datamanager.entities.audit;

import com.fasterxml.jackson.annotation.JsonFormat;
import edu.kit.datamanager.entities.dc40.Agent;
import edu.kit.datamanager.entities.dc40.Identifier;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;

/**
 *
 * @author jejkal
 */
@ApiModel(description = "Audit Event")
public class AuditEvent {

    public enum TYPE {

        CREATION,
        INGESTION,
        METADATA_MODIFICATION,
        CONTENT_MODIFICATION,
        CONTENT_REMOVAL,
        MIGRATION,
        REPLICATION,
        VALIDATION,
        DERIVATIVE_CREATION,
        DELETION,
        DEACCESSION;
    }

    public enum TRIGGER {
        INTERNAL,
        EXTERNAL;
    }

    //internal event id
    private int _id;
    @ApiModelProperty(value = "The date when the event occured.", example = "2017-05-10T10:41:00Z", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "GMT")
    private Date eventDate;
    @ApiModelProperty(value = "The resource identifier this event is associated with.", required = true)
    private Identifier resourceId;
    @ApiModelProperty(value = "The event type.", required = true)
    private TYPE eventType;
    @ApiModelProperty(value = "The trigger which created the event.", required = false)
    private TRIGGER eventTrigger;// INTERNAL or EXTERNAL
    @ApiModelProperty(value = "The event category that can be used to group events.", required = false)
    private String category;//e.g. audit.digitalObject
    @ApiModelProperty(value = "The agent who created the event.", required = false)
    private Agent agent;
    @ApiModelProperty(value = "An optional list of audit details describing the event.", required = false)
    private Set<AuditDetail> details;

    /**
     * Default constructor.
     */
    public AuditEvent() {
    }

    /**
     * Factory an audit event of the provided type in the provided category.
     * Inside this call, the event date is set to now().
     *
     * @param type The event type.
     * @param category The event category.
     *
     * @return The audit event.
     */
    public static AuditEvent factoryAuditEvent(TYPE type, String category) {
        AuditEvent entry = new AuditEvent();
        entry.setCategory(category);
        entry.setEventType(type);

        entry.setEventDate(new Date());
        return entry;
    }

    /**
     * Get the event date.
     *
     * @return The event date.
     */
    public Date getEventDate() {
        return eventDate;
    }

    /**
     * Set the event date.
     *
     * @param eventDate The event date.
     */
    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    /**
     * Set the resource id.
     *
     * @param resourceId The resource id.
     */
    public void setResourceId(Identifier resourceId) {
        this.resourceId = resourceId;
    }

    /**
     * Get the resource id.
     *
     * @return The resource id.
     */
    public Identifier getResourceId() {
        return resourceId;
    }

    /**
     * Get the event type.
     *
     * @return The event type.
     */
    public TYPE getEventType() {
        return eventType;
    }

    /**
     * Set the event type.
     *
     * @param eventType The event type.
     */
    public void setEventType(TYPE eventType) {
        this.eventType = eventType;
    }

    public void setEventTrigger(TRIGGER eventTrigger) {
        this.eventTrigger = eventTrigger;
    }

    public TRIGGER getEventTrigger() {
        return eventTrigger;
    }

    /**
     * Get the category.
     *
     * @return The category.
     */
    public String getCategory() {
        return category;
    }

    /**
     * Set the category.
     *
     * @param category The category.
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Set the agent.
     *
     * @param agent The agent.
     */
    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    /**
     * Get the agent.
     *
     * @return The agent.
     */
    public Agent getAgent() {
        return agent;
    }

    /**
     * Get event details.
     *
     * @return The event details.
     */
    public Set<AuditDetail> getDetails() {
        if (details == null) {
            details = new HashSet<>();
        }
        return details;
    }

    /**
     * Set event details.
     *
     * @param details The event details.
     */
    public void setDetails(Set<AuditDetail> details) {
        this.details = details;
    }

    /**
     * Add one or more audit details.
     *
     * @param details An array of audit details.
     */
    public void addAuditDetails(AuditDetail... details) {
        getDetails().addAll(Arrays.asList(details));
    }

    /**
     * Add one audit details.
     *
     * @param detail An audit details.
     */
    public void addAuditDetail(AuditDetail detail) {
        addAuditDetails(detail);
    }

    /**
     * Add an audit detail of type ARGUMENT. Therefor, at least type and name
     * should be provided.
     *
     * @param type The argument type.
     * @param name The argument name.
     */
    public void addArgumentDetail(String type, String name) {
        addAuditDetail(AuditDetail.factoryArgumentDetail(type, name));
    }

    /**
     * Add an audit detail of type ARGUMENT. Therefor, at least type and name
     * should be provided. The value is options.
     *
     * @param type The argument type.
     * @param name The argument name.
     * @param value The argument value.
     */
    public void addArgumentDetail(String type, String name, String value) {
        addAuditDetail(AuditDetail.factoryArgumentDetail(type, name, value));
    }

    /**
     * Add an audit detail of type COMMENT. Therefor, only value must be
     * provided.
     *
     * @param value The comment value.
     */
    public void addCommentDetail(String value) {
        addAuditDetail(AuditDetail.factoryCommentDetail(value));
    }

    @Override
    public String toString() {
        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        StringBuilder b = new StringBuilder();
        b.append("EventType: ").append(getEventType()).append("\n");
        b.append("EventDate: ").append(df.format(getEventDate())).append("\n");
        b.append("Category: ").append(getCategory()).append("\n");
        b.append("Agent: ").append(getAgent()).append("\n");
        b.append("Details: ").append(getDetails());
        return b.toString();
    }

}

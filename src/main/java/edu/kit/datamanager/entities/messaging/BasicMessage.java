/*
 * Copyright 2018 Karlsruhe Institute of Technology.
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
package edu.kit.datamanager.entities.messaging;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.kit.datamanager.exceptions.MessageValidationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Data;
import org.joda.time.DateTimeZone;
import org.joda.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jejkal
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BasicMessage implements IAMQPSubmittable {

    private static final Logger LOGGER = LoggerFactory.getLogger(BasicMessage.class);

    private String principal;
    private String sender;
    private long timestamp;

    private String entityId;
    //list of addressees for internal use, e.g. if triggering a dedicated handler 
    private Set<String> addressees = new HashSet<>();
    private String action;
    @JsonInclude(Include.NON_NULL)
    private String subCategory;
    @JsonInclude(Include.NON_NULL)
    private Map<String, String> metadata = new HashMap<>();
    @JsonInclude(Include.NON_NULL)
    private MessagePayload[] payload = new MessagePayload[]{};
    @JsonIgnore
    private List<MessagePayload> tmpPayload = new ArrayList<>();

    @JsonIgnore
    public String getEntityName() {
        return "generic";
    }

    @JsonIgnore
    @Override
    public final String getRoutingKey() {
        String routingKey = getEntityName().toLowerCase() + "." + getAction().toLowerCase();
        if (getSubCategory() != null) {
            routingKey += "." + getSubCategory().toLowerCase();
        }
        return routingKey;
    }

    @Override
    public void validate() {
        boolean valid = true;
        StringBuilder message = new StringBuilder();
        String mustNotBeNull = " must not be null!\n";
        if (getEntityName() == null) {
            valid = false;
            message.append("Entity name").append(mustNotBeNull);
        }

        if (getAction() == null) {
            valid = false;
            message.append("Action").append(mustNotBeNull);
        }

        if (getEntityId() == null) {
            valid = false;
            message.append("Entity id").append(mustNotBeNull);
        }
        if (!valid) {
            LOGGER.trace(message.toString());
            throw new MessageValidationException(message.toString());
        }
    }

    public void addPayload(String payloadId, String mimeType, byte[] payload) {
        tmpPayload.add(new MessagePayload(payloadId, mimeType, payload));
    }

    public final void setCurrentTimestamp() {
        setTimestamp(Instant.now().toDateTime(DateTimeZone.UTC).getMillis());
    }

    @Override
    public String toJson() throws JsonProcessingException {
        payload = tmpPayload.toArray(new MessagePayload[]{});
        return IAMQPSubmittable.super.toJson();
    }

    public static BasicMessage fromJson(String jsonString) throws IOException {
        return new ObjectMapper().readValue(jsonString, BasicMessage.class);
    }
    
    public static void main(String[] args) throws Exception{
        BasicMessage msg = new BasicMessage();
        msg.setAction("read");
        msg.setEntityId("1234");
        msg.setSubCategory("data");
        msg.setPrincipal("thomas");
        msg.addPayload("contentInformation", "application/json", "{\"test\":\"Success\"}".getBytes());
        String json = msg.toJson();
        System.out.println(json);
        
        BasicMessage m2 = BasicMessage.fromJson(msg.toJson());
        System.out.println(new String(m2.getPayload()[0].getPayload()));
        
    }
}

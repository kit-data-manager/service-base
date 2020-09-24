package edu.kit.datamanager.entities.messaging;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PidRecordMessage extends BasicMessage {

    // A url-string that will simply resolve the pid.
    public static final String RESOLVING_URL = "resolvingUrl";
    public static final String PID = "pid";

    public enum ACTION {
        ADD("add"),
        UPDATE("update");

        private final String value;

        ACTION(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return getValue();
        }
    }

    public enum SUB_CATEGORY {
        PROFILE_TESTBED4INF("p_testbed"),
        PROFILE_HMC("p_hmc");

        private final String value;

        SUB_CATEGORY(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return getValue();
        }
    }

    @Override
    public String getEntityName() {
        return "pidrecord";
    }

    public static PidRecordMessage creation(
            String pid,
            String recordUrl,
            String principal,
            String sender)
    {
        Map<String, String> properties = new HashMap<>();
        properties.put(RESOLVING_URL, recordUrl);
        return recordMessage(pid, properties, ACTION.ADD, Optional.empty(), principal, sender);
    }

    public static PidRecordMessage update(
            String pid,
            String recordUrl,
            String principal,
            String sender)
    {
        Map<String, String> properties = new HashMap<>();
        properties.put(RESOLVING_URL, recordUrl);
        return recordMessage(pid, properties, ACTION.UPDATE, Optional.empty(), principal, sender);
    }

    public static PidRecordMessage recordMessage(
            String entityId,
            Map<String, String> properties,
            ACTION action,
            Optional<SUB_CATEGORY> subCategory,
            String principal,
            String sender)
    {
        PidRecordMessage msg = new PidRecordMessage();
        msg.setEntityId(entityId);
        msg.setAction(action.getValue());
        subCategory.map(
            sub -> { msg.setSubCategory(sub.getValue()); return sub; }
        );
        msg.setPrincipal(principal);
        msg.setSender(sender);
        if (properties != null) {
            msg.setMetadata(properties);
        }
        msg.setCurrentTimestamp();
        return msg;
    }
}

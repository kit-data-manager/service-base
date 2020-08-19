package edu.kit.datamanager.entities.messaging;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PidRecordMessage extends BasicMessage {

    // A url-string that will simply resolve the pid.
    public static final String RESOLVING_URL = "resolvingUrl";

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

    public static PidRecordMessage recordCreationMessage(String pid, String url, String principal, String sender) {
        return recordMessage(pid, url, ACTION.ADD, principal, sender);
    }

    public static PidRecordMessage recordUpdateMessage(String pid, String url, String principal, String sender) {
        return recordMessage(pid, url, ACTION.UPDATE, principal, sender);
    }

    public static PidRecordMessage recordMessage(String pid, String url, ACTION action, String principal, String sender) {
        Map<String, String> properties = new HashMap<>();
        properties.put(RESOLVING_URL, url);
        
        PidRecordMessage msg = new PidRecordMessage();
        msg.setEntityId(pid);
        msg.setAction(action.getValue());
        msg.setPrincipal(principal);
        msg.setSender(sender);
        msg.setMetadata(properties);
        msg.setCurrentTimestamp();
        return msg;
    }

    public static PidRecordMessage recordMessage(String pid, ACTION action, SUB_CATEGORY subCategory,
            Map<String, String> properties, String principal, String sender) {
        PidRecordMessage msg = new PidRecordMessage();
        msg.setEntityId(pid);
        msg.setAction(action.getValue());
        msg.setSubCategory(subCategory.getValue());
        msg.setPrincipal(principal);
        msg.setSender(sender);
        if (properties != null) {
            msg.setMetadata(properties);
        }
        msg.setCurrentTimestamp();
        return msg;
    }
}
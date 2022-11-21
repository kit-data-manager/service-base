/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.kit.datamanager.entities.messaging;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 *
 * @author jejkal
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessagePayload {

    private String payloadId;
    private String mimeType;
    private byte[] payload;

    public MessagePayload() {
    }

    public MessagePayload(String payloadId, String mimeType, byte[] payload) {
        this.payloadId = payloadId;
        this.mimeType = mimeType;
        this.payload = payload;
    }
}

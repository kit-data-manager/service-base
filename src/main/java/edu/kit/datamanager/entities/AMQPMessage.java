/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.kit.datamanager.entities;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.kit.datamanager.entities.messaging.IAMQPSubmittable;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;

/**
 * Very simple AMQP message entity holding the target exchange and routingKey as
 * well as the already JSON-serialized message.
 *
 * @author Jejkal
 */
@Entity
@Data
public class AMQPMessage implements IAMQPSubmittable, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    private String exchange;
    private String routingKey;
    private String message;

    public AMQPMessage() {
    }

    public AMQPMessage(String exchange, String routingKey, String message) {
        this.exchange = exchange;
        this.routingKey = routingKey;
        this.message = message;
    }

    @Override
    public void validate() {
        //always successful as the input comes from a valid IAMQPSubmittable
    }

    @Override
    public String toJson() throws JsonProcessingException {
        //no fancy stuff here, only return the message which was already serialized by the original IAMQPSubmittable
        return message;
    }

}

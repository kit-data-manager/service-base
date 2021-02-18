/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.kit.datamanager.dao;

import edu.kit.datamanager.entities.AMQPMessage;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA Repository for storing AMQP messages temporarily in case they could not
 * be submitted to the broker.
 *
 * @author Jejkal
 */
public interface IAMQPMessageDao extends JpaRepository<AMQPMessage, Long> {

}

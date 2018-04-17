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
package edu.kit.datamanager.service.exceptions;

/**
 *
 * @author jejkal
 */
public class ServiceException extends Exception{

//  private final int statusCode;
//
//  /**
//   * Default constructor taking the response status and setting the reason
//   * phrase as message.
//   *
//   * @param status The response status.
//   */
//  public ServiceException(Response.Status status){
//    this(status.getStatusCode());
//  }
//
//  /**
//   * Default constructor wrapping any exception that might be produced by a
//   * service adapter implementation.
//   *
//   * @param status The response status.
//   * @param message An adapter-specific error message.
//   */
//  public ServiceException(Response.Status status, String message){
//    this(status.getStatusCode(), message);
//  }
//
//  /**
//   * Default constructor wrapping any exception that might be produced by a
//   * service adapter implementation.
//   *
//   * @param status The response status.
//   * @param message An adapter-specific error message.
//   * @param cause The service error causing this exception.
//   */
//  public ServiceException(Response.Status status, String message, Throwable cause){
//    this(status.getStatusCode(), message, cause);
//  }
//
//  /**
//   * Default constructor taking the response status and setting the reason
//   * phrase as message.
//   *
//   * @param statusCode The response status.
//   */
//  public ServiceException(int statusCode){
//    this(statusCode, "Failed with HTTP " + statusCode);
//  }
//
//  /**
//   * Default constructor wrapping any exception that might be produced by a
//   * service adapter implementation.
//   *
//   * @param statusCode The response status.
//   * @param message An adapter-specific error message.
//   */
//  public ServiceException(int statusCode, String message){
//    this(statusCode, message, null);
//  }
//
//  /**
//   * Default constructor wrapping any exception that might be produced by a
//   * service adapter implementation.
//   *
//   * @param statusCode The response status.
//   * @param message An adapter-specific error message.
//   * @param cause The service error causing this exception.
//   */
//  public ServiceException(int statusCode, String message, Throwable cause){
//    super(message, cause);
//    this.statusCode = statusCode;
//  }
//
//  public int getStatusCode(){
//    return statusCode;
//  }
//
//  /**
//   * Throws a WebApplicationException equivalent for this service exception.
//   * This default implementation just returns the status HTTP INTERNAL SERVER
//   * ERROR (500), but should be overwritten to map a certain service exception
//   * to a proper HTTP status code.
//   *
//   */
//  public void throwWebApplicationException(){
//    throw new WebApplicationException(Response.status(statusCode).entity(getMessage()).type(MediaType.TEXT_PLAIN).build());
//  }

}

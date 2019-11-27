package edu.kit.datamanager.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Map;

public interface IVersioningService {
	
	void configure();

    /**
     * adds the file of an object to the default OCFL repository.
     * 
     * @param resourceId
     *            identifier of the object
     * @param callerId
     *            name of the user
     * @param path
     *            path of the file
     * @param data
     *            the file
     * @param options
     *            contains three keys: finalize, token number and parent.
     * @return OcflObjectIdentifier : resourceId, versionId, parent version, finalize, token
     */
    void write(String resourceId, String callerId, Path path, InputStream data,
            Map<String, String> options) throws IOException;

    /**
     * returns files of an object's version.
     * 
     * @param resourceId
     * @param callerId
     * @param path
     * @param versionId
     * @param destination
     * @param options
     * @return
     */
    void read(String resourceId, String callerId, Path path, String versionId, InputStream destination,
            Map<String, String> options) throws IOException;

    /**
     * returns information for a specific resource
     * 
     * @param resourceId
     * @param path
     * @param versionId
     * @param options
     * @return
     */
    void info(String resourceId, Path path, String versionId, Map<String, String> options);
}
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
package edu.kit.datamanager.util;

/**
 *
 * @author jejkal
 */
public class TestUtils {

//    private static final Logger LOGGER = LoggerFactory.getLogger(TestUtils.class);
//
//    public static Response login(JerseyTest test, String authKey, String authToken, String groupId, int expectedStatus) {
//        Session newSession = new Session();
//        newSession.setSessionId(UUID.randomUUID().toString());
//        newSession.setGroupId(groupId);
//        Response response;
//        if (authKey != null && authToken != null) {
//            response = test.target("auth/sessions/").queryParam(authKey, authToken).request().post(Entity.entity(newSession, MediaType.APPLICATION_JSON));
//        } else {
//            response = test.target("auth/sessions/").request().post(Entity.entity(newSession, MediaType.APPLICATION_JSON));
//        }
//        if (expectedStatus != -1 && response.getStatus() == expectedStatus) {
//            return response;
//        } else if (expectedStatus != -1) {
//            throw new RuntimeException("Invalid response status " + response.getStatus());
//        }
//        if (response.getStatus() != 201 && response.getStatus() != 200) {
//            LOGGER.warn("Session creation returned with status {} and message {}. Following calls may fail.", response.getStatus(), response.readEntity(String.class));
//            throw new RuntimeException("Invalid response status " + response.getStatus());
//        }
//        return response;
//    }
//
//    public static Session login(String userId, String groupId) {
//        ISessionServiceAdapter sessionAdapter = ServiceUtil.getService(ISessionServiceAdapter.class);
//        Session newSession = Session.createSession(userId, groupId);
//        
//        try {
//            Session existing = sessionAdapter.getSessionByUserId(userId);
//            if (existing != null) {
//                return existing;
//            }
//
//            return sessionAdapter.create(newSession);
//        } catch (ServiceException ex) {
//            LOGGER.warn("Session creation returned with status {} and message {}. Following calls may fail.", ex.getStatusCode(), ex.getMessage());
//            throw new RuntimeException("Invalid response status " + ex.getStatusCode());
//        }
//    }
//
//    public static Response login(JerseyTest test, String authKey, String authToken, String groupId) {
//        return login(test, authKey, authToken, groupId, -1);
//    }
//
//    public static void addUserToGroup(JerseyTest test, String userId, String groupId) throws IOException {
//        Response response = TestUtils.login(test, "token", "open", "SYSTEM");
//        Session session = response.readEntity(Session.class);
//
//        response = test.target("auth/groups/" + groupId).request().header("Session-Id", session.getSessionId()).get();
//        EntityTag tag = response.getEntityTag();
//        //patch name
//        //apply patch changing group to SYSTEM
//        CloseableHttpClient httpclient = HttpClients.custom()
//                .setRedirectStrategy(new LaxRedirectStrategy()) // adds HTTP REDIRECT support to GET and POST methods 
//                .build();
//
//        HttpPatch httpPatch = new HttpPatch(URI.create("http://localhost:9998/auth/groups/" + groupId));
//        httpPatch.addHeader("Content-Type", "application/json-patch+json");
//        httpPatch.addHeader("Accept", "application/json");
//        httpPatch.addHeader("Session-Id", session.getSessionId());
//        httpPatch.addHeader("If-None-Match", tag.toString());
//        httpPatch.setEntity(EntityBuilder.create().setText("[{\"op\": \"add\", \"path\":\"/memberIdentifiers/1\", \"value\": \"" + userId + "\"}]").build());
//        int status = 0;
//        try (CloseableHttpResponse res = httpclient.execute(httpPatch)) {
//            status = res.getStatusLine().getStatusCode();
//        } finally {
//            httpclient.close();
//        }
//        Assert.assertEquals(Response.Status.NO_CONTENT, Response.Status.fromStatusCode(status));
//        response = test.target("auth/groups/" + groupId).request().header("Session-Id", session.getSessionId()).get();
//    }
}

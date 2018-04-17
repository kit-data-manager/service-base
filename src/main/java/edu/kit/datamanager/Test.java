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
package edu.kit.datamanager;

/**
 *
 * @author jejkal
 */
public final class Test{

  //   private static final ServiceLocator LOCATOR = ServiceLocatorFactory.getInstance().create(null);
  // try {
  /*  DynamicConfigurationService dcs = ServiceLocatorFactory.getInstance().create(null).getService(DynamicConfigurationService.class);
            Populator populator = dcs.getPopulator();
            populator.populate();*/
 /* } catch (IOException | MultiException e) {
            throw new MultiException(e);
        }*/
 /*   HK2Populator.populate(LOCATOR);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
  public static void main(String[] args) throws Exception{
    System.out.println(Integer.toString(1 | 2));

    // ArangoDB arangoDB = new ArangoDB.Builder().user("root").build();
//        String dbName = "datamanager";
//        String collectionName = "acl";
//        // String collectionName = "dataResources";
    /*  
        try {
            arangoDB.createDatabase(dbName);
            System.out.println("Database created: " + dbName);
        } catch (ArangoDBException e) {
            System.err.println("Failed to create database: " + dbName + "; " + e.getMessage());
        }
     */
 /*   try {
            CollectionEntity myArangoCollection = arangoDB.db(dbName).createCollection(collectionName);
            System.out.println("Collection created: " + myArangoCollection.getName());
        } catch (ArangoDBException e) {
            System.err.println("Failed to create collection: " + collectionName + "; " + e.getMessage());
        }
     */
 /*   ResourceAcl acl = new ResourceAcl();
        // acl.setKey("bla");
        acl.setResourceId("test123");
        User a = new User();
        a.setIdentifier("user1");
        a.setAffiliation("KIT");
        acl.getUserAcl().put(a, ResourceAcl.Permission.READ);

        Group g = new Group();
        g.setIdentifier("group1");
        g.setDescription("KIT Group");
        acl.getGroupAcl().put(g, ResourceAcl.Permission.APPEND);
        String key = "";
        try {
            DocumentCreateEntity<ResourceAcl> result = arangoDB.db(dbName).collection(collectionName).insertDocument(acl);
            key = result.getKey();
            System.out.println("Document created " + key);
        } catch (ArangoDBException ex) {
            System.err.println("Failed to create document. " + ex.getMessage());
        }

        try {
            ResourceAcl resource = arangoDB.db(dbName).collection(collectionName).getDocument(key, ResourceAcl.class);

            System.out.println("Document obtained");
            System.out.println(resource.getKey());
            System.out.println(resource.getResourceId());
            System.out.println(resource.getUserAcl().keySet().iterator().next().getAffiliation());
            System.out.println(resource.getGroupAcl().keySet().iterator().next().getDescription());
        } catch (ArangoDBException exx) {
            System.err.println("Failed to create document. " + exx.getMessage());
        }*/

 /* ArangoCursor<ResourceAcl> results = arangoDB.db(dbName).query("FOR a IN acl"
                + "  LET keys = ( "
                + "     FOR e IN a.groupAcl"
                + "        LET elems = ("
                + "            FOR k IN e.key"
                + "            FILTER k.expiresAt == -1"
                + "            RETURN k"
                +"            )"
                 + "  FILTER LENGTH(elems) > 0 "
                 + "  RETURN elems "
                + "  )"
                + "  FILTER LENGTH(keys) > 0 "
                + "RETURN keys[0]", null, null, ResourceAcl.class);
     */
//        ArangoCursor<ResourceAcl> results = arangoDB.db(dbName).query(
//                "FOR a IN `acl`\n"
//                + "  FOR gacl IN a.groupAcl"
//                + "    FOR k IN gacl.key"
//                + "       RETURN k.expiresAt", null, null, ResourceAcl.class);
//
//        for (ResourceAcl a : results.asListRemaining()) {
//            System.out.println(a.getGroupAcl());
//        }
//
//        arangoDB.shutdown();
  }
}

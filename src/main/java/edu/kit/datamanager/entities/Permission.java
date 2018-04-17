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
package edu.kit.datamanager.entities;

/**
 *
 * @author jejkal
 */
public enum Permission {
    //no access
    NONE,
    //allow HTTP GET
    READ,
    //allow all from Permission.READ and HTTP POST (?), HTTP PATH (only adding information)
    //does not allow replace or remove information!
    APPEND,
    //allow all from Permission.APPEND and HTTP PUT, HTTP POST, HTTP DELETE, HTTP PATCH
    WRITE;

    public boolean lessOrEqual(Permission permission) {
        if (permission == null) {
            return false;
        }
        return this.ordinal() <= permission.ordinal();
    }

    public boolean less(Permission permission) {
        if (permission == null) {
            return false;
        }
        return this.ordinal() < permission.ordinal();
    }

    public boolean eq(Permission permission) {
        if (permission == null) {
            return false;
        }
        return this.ordinal() == permission.ordinal();
    }

    public static Permission min(Permission p1, Permission p2) {
        if (!p1.eq(p2)) {
            if (p1.ordinal() > p2.ordinal()) {
                //return p2 only if p1 > p2
                return p2;
            }
        }

        //default result is p1 (returned if p1 == p2 or if p1 < p2)
        return p1;
    }

    public static Permission max(Permission p1, Permission p2) {
        if (!p1.eq(p2)) {
            if (p1.ordinal() < p2.ordinal()) {
                //return p2 only if p1 > p2
                return p2;
            }
        }

        //default result is p1 (returned if p1 == p2 or if p1 < p2)
        return p1;
    }

}

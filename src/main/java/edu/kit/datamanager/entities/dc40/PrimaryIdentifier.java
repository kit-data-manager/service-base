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
package edu.kit.datamanager.entities.dc40;

/**
 *
 * @author jejkal
 */
public class PrimaryIdentifier extends Identifier {

    public static PrimaryIdentifier factoryPrimaryIdentifier() {
        return factoryPrimaryIdentifier(UnknownInformationConstants.TO_BE_ASSIGNED_OR_ANNOUNCED_LATER.getValue());
    }

    public static PrimaryIdentifier factoryPrimaryIdentifier(String doiOrTbaConstant) {
        PrimaryIdentifier result = new PrimaryIdentifier();
        result.setIdentifierType(IDENTIFIER_TYPE.DOI);
        result.setValue(doiOrTbaConstant);
        return result;
    }

    @Override
    public void setIdentifierType(IDENTIFIER_TYPE identifierType) {
        if (!IDENTIFIER_TYPE.DOI.equals(identifierType)) {
            throw new IllegalArgumentException("Identifier type of primary identifier must be " + IDENTIFIER_TYPE.DOI);
        }
        super.setIdentifierType(identifierType);
    }

    public boolean hasDoi() {
        return !UnknownInformationConstants.TO_BE_ASSIGNED_OR_ANNOUNCED_LATER.equals(getValue());
    }

}

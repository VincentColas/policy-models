/*-
 * ============LICENSE_START=======================================================
 * ONAP Policy Model
 * ================================================================================
 * Copyright (C) 2019 AT&T Intellectual Property. All rights reserved.
 * Modifications Copyright (C) 2019 Nordix Foundation.
 * ================================================================================
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
 *
 * SPDX-License-Identifier: Apache-2.0
 * ============LICENSE_END=========================================================
 */

package org.onap.policy.models.tosca.authorative.concepts;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Class to represent TOSCA policy type matching input/output from/to client.
 *
 * @author Chenfei Gao (cgao@research.att.com)
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ToscaPolicyType extends ToscaEntity implements Comparable<ToscaPolicyType> {
    private Map<String, ToscaProperty> properties;

    /**
     * Copy Constructor.
     *
     * @param copyObject object to copy from
     */
    public ToscaPolicyType(@NonNull ToscaPolicyType copyObject) {
        super(copyObject);

        if (copyObject.properties != null) {
            properties = new LinkedHashMap<>();
            for (final Entry<String, ToscaProperty> propertyEntry : copyObject.properties.entrySet()) {
                properties.put(propertyEntry.getKey(), propertyEntry.getValue());
            }
        }
    }

    @Override
    public int compareTo(final ToscaPolicyType other) {
        return compareNameVersion(this, other);
    }
}

/*-
 * ============LICENSE_START=======================================================
 * ONAP Policy Model
 * ================================================================================
 * Copyright (C) 2019 AT&T Intellectual Property. All rights reserved.
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

package org.onap.policy.models.tosca;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Class to represent custom data type in TOSCA definition.
 *
 * @author Chenfei Gao (cgao@research.att.com)
 *
 */
@ToString
public class ToscaDataType {
    @Getter
    @Setter
    @SerializedName("derived_from")
    private String derivedFrom;

    @Getter
    @Setter
    @SerializedName("version")
    private String version;

    @Getter
    @Setter
    @SerializedName("metadata")
    private Map<String, String> metadata;

    @Getter
    @Setter
    @SerializedName("description")
    private String description;

    @Getter
    @Setter
    @SerializedName("constraints")
    private List<ToscaConstraint> constraints;

    @Getter
    @Setter
    @SerializedName("properties")
    private List<Map<String, ToscaProperty>> properties;
}
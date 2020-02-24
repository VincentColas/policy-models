/*-
 * ============LICENSE_START=======================================================
 * ONAP
 * ================================================================================
 * Copyright (C) 2020 AT&T Intellectual Property. All rights reserved.
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
 * ============LICENSE_END=========================================================
 */

package org.onap.policy.controlloop.actor.so;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.onap.policy.common.parameters.annotations.Min;
import org.onap.policy.controlloop.actorserviceprovider.parameters.HttpActorParams;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class SoActorParams extends HttpActorParams {

    /*
     * Optional, default values that are used if missing from the operation-specific
     * parameters.
     */

    /**
     * Path to use for the "get" request.
     */
    private String pathGet = "/orchestrationRequests/v5/";

    /**
     * Maximum number of "get" requests permitted, after the initial request, to retrieve
     * the response.
     */
    @Min(0)
    private int maxGets = 20;

    /**
     * Time, in seconds, to wait between issuing "get" requests.
     */
    @Min(1)
    private int waitSecGet = 20;
}
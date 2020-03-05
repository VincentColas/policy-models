/*-
 * ============LICENSE_START=======================================================
 * SdnrOperation
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

package org.onap.policy.controlloop.actor.sdnr;

import java.util.concurrent.CompletableFuture;
import org.onap.policy.controlloop.actorserviceprovider.OperationOutcome;
import org.onap.policy.controlloop.actorserviceprovider.parameters.BidirectionalTopicConfig;
import org.onap.policy.controlloop.actorserviceprovider.parameters.ControlLoopOperationParams;
import org.onap.policy.sdnr.PciRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModifyConfigOperation extends SdnrOperation {
    private static final Logger logger = LoggerFactory.getLogger(ModifyConfigOperation.class);

    public static final String NAME = "ModifyConfig";

    /**
     * Constructs the object.
     *
     * @param params operation parameters
     * @param config configuration for this operation
     */
    public ModifyConfigOperation(ControlLoopOperationParams params, BidirectionalTopicConfig config) {
        super(params, config);
    }

    @Override
    protected PciRequestWrapper makeRequest(int attempt) {
        PciRequestWrapper request = super.makeRequest(attempt);
        //
        // Set the recipe and action information
        //
        request.setRpcName(NAME.toLowerCase());
        request.getBody().setAction(NAME);
        logger.info("SDNR ModifyConfig Request to be sent is {}", request);
        return request;
    }
}
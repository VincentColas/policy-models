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

package org.onap.policy.controlloop.actorserviceprovider.impl;

import java.util.Map;
import java.util.function.Function;
import org.onap.policy.controlloop.actorserviceprovider.Util;
import org.onap.policy.controlloop.actorserviceprovider.parameters.HttpActorParams;

/**
 * Actor that uses HTTP, where the only additional property that an operator needs is a
 * URL. The actor's parameters must be an {@link HttpActorParams} and its operator
 * parameters are expected to be an {@link HttpParams}.
 */
public class HttpActor extends ActorImpl {

    /**
     * Constructs the object.
     *
     * @param name actor's name
     */
    public HttpActor(String name) {
        super(name);
    }

    /**
     * Translates the parameters to an {@link HttpActorParams} and then creates a function
     * that will extract operator-specific parameters.
     */
    @Override
    protected Function<String, Map<String, Object>> makeOperatorParameters(Map<String, Object> actorParameters) {
        String actorName = getName();

        // @formatter:off
        return Util.translate(actorName, actorParameters, HttpActorParams.class)
                        .doValidation(actorName)
                        .makeOperationParameters(actorName);
        // @formatter:on
    }
}

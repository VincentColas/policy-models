/*-
 * ============LICENSE_START=======================================================
 *  Copyright (C) 2019 Nordix Foundation.
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

package org.onap.policy.models.provider.impl;

import lombok.NonNull;
import org.onap.policy.models.provider.PolicyModelsProviderParameters;
import org.onap.policy.models.tosca.authorative.concepts.ToscaServiceTemplate;

/**
 * Sub class to check getDummyResponse() method in base class.
 *
 * @author Liam Fallon (liam.fallon@est.tech)
 */
public class DummyPolicyModelsProviderSubImpl extends DummyPolicyModelsProviderImpl {
    /**
     * Constructor.
     *
     * @param parameters the parameters
     */
    public DummyPolicyModelsProviderSubImpl(@NonNull PolicyModelsProviderParameters parameters) {
        super(parameters);
    }

    public ToscaServiceTemplate getBadDummyResponse1() {
        return super.getDummyResponse("/i/dont/exist");
    }

    public ToscaServiceTemplate getBadDummyResponse2() {
        return super.getDummyResponse(null);
    }
}

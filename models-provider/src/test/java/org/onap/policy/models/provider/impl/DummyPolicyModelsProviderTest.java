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

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.onap.policy.models.pdp.concepts.PdpGroups;
import org.onap.policy.models.provider.PolicyModelsProvider;
import org.onap.policy.models.provider.PolicyModelsProviderFactory;
import org.onap.policy.models.provider.PolicyModelsProviderParameters;
import org.onap.policy.models.tosca.authorative.concepts.ToscaServiceTemplate;
import org.onap.policy.models.tosca.legacy.concepts.LegacyGuardPolicyInput;
import org.onap.policy.models.tosca.legacy.concepts.LegacyOperationalPolicy;

/**
 * Test the dummy models provider implementation.
 *
 * @author Liam Fallon (liam.fallon@est.tech)
 */
public class DummyPolicyModelsProviderTest {

    @Test
    public void testProvider() throws Exception {
        PolicyModelsProviderParameters parameters = new PolicyModelsProviderParameters();
        parameters.setImplementation(DummyPolicyModelsProviderImpl.class.getCanonicalName());
        parameters.setDatabaseUrl("jdbc:dummy");
        parameters.setPersistenceUnit("dummy");

        PolicyModelsProvider dummyProvider = new PolicyModelsProviderFactory().createPolicyModelsProvider(parameters);

        dummyProvider.init();

        ToscaServiceTemplate serviceTemplate = dummyProvider.getPolicies("onap.vcpe.tca", "1.0.0");
        assertNotNull(serviceTemplate);
        assertEquals("onap.policies.monitoring.cdap.tca.hi.lo.app",
                serviceTemplate.getToscaTopologyTemplate().getPolicies().get(0).get("onap.vcpe.tca").getType());

        dummyProvider.close();
    }

    @Test
    public void testProviderMethods() throws Exception {
        PolicyModelsProviderParameters parameters = new PolicyModelsProviderParameters();
        parameters.setImplementation(DummyPolicyModelsProviderImpl.class.getCanonicalName());
        parameters.setDatabaseUrl("jdbc:dummy");
        parameters.setPersistenceUnit("dummy");

        PolicyModelsProvider dummyProvider = new PolicyModelsProviderFactory().createPolicyModelsProvider(parameters);
        dummyProvider.init();

        assertNotNull(dummyProvider.getPolicyTypes("name", "version"));
        assertNotNull(dummyProvider.createPolicyTypes(new ToscaServiceTemplate()));
        assertNotNull(dummyProvider.updatePolicyTypes(new ToscaServiceTemplate()));
        assertNotNull(dummyProvider.deletePolicyTypes("name", "version"));

        assertNotNull(dummyProvider.getPolicies("name", "version"));
        assertNotNull(dummyProvider.createPolicies(new ToscaServiceTemplate()));
        assertNotNull(dummyProvider.updatePolicies(new ToscaServiceTemplate()));
        assertNotNull(dummyProvider.deletePolicies("name", "version"));

        assertNotNull(dummyProvider.getOperationalPolicy("policy_id"));
        assertNotNull(dummyProvider.createOperationalPolicy(new LegacyOperationalPolicy()));
        assertNotNull(dummyProvider.updateOperationalPolicy(new LegacyOperationalPolicy()));
        assertNotNull(dummyProvider.deleteOperationalPolicy("policy_id"));

        assertNotNull(dummyProvider.getGuardPolicy("policy_id"));
        assertNotNull(dummyProvider.createGuardPolicy(new LegacyGuardPolicyInput()));
        assertNotNull(dummyProvider.updateGuardPolicy(new LegacyGuardPolicyInput()));
        assertNotNull(dummyProvider.deleteGuardPolicy("policy_id"));

        assertNotNull(dummyProvider.getPdpGroups("filter"));
        assertNotNull(dummyProvider.createPdpGroups(new PdpGroups()));
        assertNotNull(dummyProvider.updatePdpGroups(new PdpGroups()));
        assertNotNull(dummyProvider.deletePdpGroups("filter"));

        assertThatThrownBy(() -> {
            dummyProvider.getPolicyTypes(null, null);
        }).hasMessage("name is marked @NonNull but is null");
        assertThatThrownBy(() -> {
            dummyProvider.createPolicyTypes(null);
        }).hasMessage("serviceTemplate is marked @NonNull but is null");
        assertThatThrownBy(() -> {
            dummyProvider.updatePolicyTypes(null);
        }).hasMessage("serviceTemplate is marked @NonNull but is null");
        assertThatThrownBy(() -> {
            dummyProvider.deletePolicyTypes(null, null);
        }).hasMessage("name is marked @NonNull but is null");

        assertThatThrownBy(() -> {
            dummyProvider.getPolicies(null, null);
        }).hasMessage("name is marked @NonNull but is null");
        assertThatThrownBy(() -> {
            dummyProvider.createPolicies(null);
        }).hasMessage("serviceTemplate is marked @NonNull but is null");
        assertThatThrownBy(() -> {
            dummyProvider.updatePolicies(null);
        }).hasMessage("serviceTemplate is marked @NonNull but is null");
        assertThatThrownBy(() -> {
            dummyProvider.deletePolicies(null, null);
        }).hasMessage("name is marked @NonNull but is null");

        assertThatThrownBy(() -> {
            dummyProvider.getOperationalPolicy(null);
        }).hasMessage("policyId is marked @NonNull but is null");
        assertThatThrownBy(() -> {
            dummyProvider.createOperationalPolicy(null);
        }).hasMessage("legacyOperationalPolicy is marked @NonNull but is null");
        assertThatThrownBy(() -> {
            dummyProvider.updateOperationalPolicy(null);
        }).hasMessage("legacyOperationalPolicy is marked @NonNull but is null");
        assertThatThrownBy(() -> {
            dummyProvider.deleteOperationalPolicy(null);
        }).hasMessage("policyId is marked @NonNull but is null");

        assertThatThrownBy(() -> {
            dummyProvider.getGuardPolicy(null);
        }).hasMessage("policyId is marked @NonNull but is null");
        assertThatThrownBy(() -> {
            dummyProvider.createGuardPolicy(null);
        }).hasMessage("legacyGuardPolicy is marked @NonNull but is null");
        assertThatThrownBy(() -> {
            dummyProvider.updateGuardPolicy(null);
        }).hasMessage("legacyGuardPolicy is marked @NonNull but is null");
        assertThatThrownBy(() -> {
            dummyProvider.deleteGuardPolicy(null);
        }).hasMessage("policyId is marked @NonNull but is null");

        assertThatThrownBy(() -> {
            dummyProvider.getPdpGroups(null);
        }).hasMessage("pdpGroupFilter is marked @NonNull but is null");
        assertThatThrownBy(() -> {
            dummyProvider.createPdpGroups(null);
        }).hasMessage("pdpGroups is marked @NonNull but is null");
        assertThatThrownBy(() -> {
            dummyProvider.updatePdpGroups(null);
        }).hasMessage("pdpGroups is marked @NonNull but is null");
        assertThatThrownBy(() -> {
            dummyProvider.deletePdpGroups(null);
        }).hasMessage("pdpGroupFilter is marked @NonNull but is null");

        dummyProvider.close();
    }

    @Test
    public void testDummyResponse() {
        DummyPolicyModelsProviderSubImpl resp = null;

        try {
            resp = new DummyPolicyModelsProviderSubImpl(new PolicyModelsProviderParameters());
            resp.getBadDummyResponse1();
            fail("test should throw an exception");
        } catch (Exception npe) {
            assertEquals("error serializing object", npe.getMessage());
        } finally {
            if (resp != null) {
                resp.close();
            }
        }

        try {
            resp = new DummyPolicyModelsProviderSubImpl(new PolicyModelsProviderParameters());
            resp.getBadDummyResponse2();
            fail("test should throw an exception");
        } catch (Exception npe) {
            assertEquals("fileName is marked @NonNull but is null", npe.getMessage());
        } finally {
            if (resp != null) {
                resp.close();
            }
        }
    }
}

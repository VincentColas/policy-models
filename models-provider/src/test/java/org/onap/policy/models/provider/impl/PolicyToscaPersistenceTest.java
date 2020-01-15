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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URISyntaxException;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.NonNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.onap.policy.common.utils.coder.CoderException;
import org.onap.policy.common.utils.coder.StandardCoder;
import org.onap.policy.common.utils.resources.ResourceUtils;
import org.onap.policy.models.base.PfModelException;
import org.onap.policy.models.provider.PolicyModelsProvider;
import org.onap.policy.models.provider.PolicyModelsProviderFactory;
import org.onap.policy.models.provider.PolicyModelsProviderParameters;
import org.onap.policy.models.tosca.authorative.concepts.ToscaPolicy;
import org.onap.policy.models.tosca.authorative.concepts.ToscaPolicyFilter;
import org.onap.policy.models.tosca.authorative.concepts.ToscaServiceTemplate;
import org.yaml.snakeyaml.Yaml;

/**
 * Test persistence of monitoring policies to and from the database.
 *
 * @author Liam Fallon (liam.fallon@est.tech)
 */
public class PolicyToscaPersistenceTest {
    private StandardCoder standardCoder;

    private PolicyModelsProvider databaseProvider;

    /**
     * Initialize provider.
     *
     * @throws PfModelException on exceptions in the tests
     * @throws CoderException on JSON encoding and decoding errors
     */
    @Before
    public void setupParameters() throws Exception {
        // H2, use "org.mariadb.jdbc.Driver" and "jdbc:mariadb://localhost:3306/policy" for locally installed MariaDB

        PolicyModelsProviderParameters parameters = new PolicyModelsProviderParameters();
        parameters.setDatabaseDriver("org.h2.Driver");
        parameters.setDatabaseUrl("jdbc:h2:mem:testdb");
        parameters.setDatabaseUser("policy");
        parameters.setDatabasePassword(Base64.getEncoder().encodeToString("P01icY".getBytes()));
        parameters.setPersistenceUnit("ToscaConceptTest");

        databaseProvider = new PolicyModelsProviderFactory().createPolicyModelsProvider(parameters);

        createPolicyTypes();
    }

    /**
     * Set up standard coder.
     */
    @Before
    public void setupStandardCoder() {
        standardCoder = new StandardCoder();
    }

    @After
    public void teardown() throws Exception {
        databaseProvider.close();
    }

    @Test
    public void testPolicyPersistence() throws Exception {
        Set<String> policyResources = ResourceUtils.getDirectoryContents("policies");

        for (String policyResource : policyResources) {
            if (!policyResource.contains("\\.tosca\\.")) {
                continue;
            }

            String policyString = ResourceUtils.getResourceAsString(policyResource);

            if (policyResource.endsWith("yaml")) {
                testYamlStringPolicyPersistence(policyString);
            } else {
                testJsonStringPolicyPersistence(policyString);
            }
        }
    }

    private void testYamlStringPolicyPersistence(final String policyString) throws Exception {
        Object yamlObject = new Yaml().load(policyString);
        String yamlAsJsonString = new StandardCoder().encode(yamlObject);

        testJsonStringPolicyPersistence(yamlAsJsonString);
    }

    /**
     * Check persistence of a policy.
     *
     * @param policyString the policy as a string
     * @throws Exception any exception thrown
     */
    public void testJsonStringPolicyPersistence(@NonNull final String policyString) throws Exception {
        ToscaServiceTemplate serviceTemplate = standardCoder.decode(policyString, ToscaServiceTemplate.class);

        assertNotNull(serviceTemplate);

        databaseProvider.createPolicies(serviceTemplate);
        databaseProvider.updatePolicies(serviceTemplate);

        for (Map<String, ToscaPolicy> policyMap : serviceTemplate.getToscaTopologyTemplate().getPolicies()) {
            for (ToscaPolicy policy : policyMap.values()) {
                ToscaServiceTemplate gotToscaServiceTemplate =
                        databaseProvider.getPolicies(policy.getName(), policy.getVersion());

                assertEquals(policy.getType(), gotToscaServiceTemplate.getToscaTopologyTemplate().getPolicies().get(0)
                        .get(policy.getName()).getType());

                gotToscaServiceTemplate = databaseProvider.getFilteredPolicies(ToscaPolicyFilter.builder().build());

                assertEquals(policy.getType(),
                        getToscaPolicyFromMapList(gotToscaServiceTemplate.getToscaTopologyTemplate().getPolicies(),
                                policy.getName()).getType());

                gotToscaServiceTemplate = databaseProvider.getFilteredPolicies(
                        ToscaPolicyFilter.builder().name(policy.getName()).version(policy.getVersion()).build());

                assertEquals(policy.getType(), gotToscaServiceTemplate.getToscaTopologyTemplate().getPolicies().get(0)
                        .get(policy.getName()).getType());
            }
        }
    }

    private ToscaPolicy getToscaPolicyFromMapList(List<Map<String, ToscaPolicy>> toscaPolicyMapList,
            String policyName) {
        ToscaPolicy toscaPolicy = new ToscaPolicy();
        for (Map<String, ToscaPolicy> policyMap : toscaPolicyMapList) {
            toscaPolicy = policyMap.get(policyName);
            if (toscaPolicy != null) {
                break;
            }
        }
        return toscaPolicy;
    }

    private void createPolicyTypes() throws CoderException, PfModelException, URISyntaxException {
        Set<String> policyTypeResources = ResourceUtils.getDirectoryContents("policytypes");

        for (String policyTypeResource : policyTypeResources) {
            Object yamlObject = new Yaml().load(ResourceUtils.getResourceAsString(policyTypeResource));
            String yamlAsJsonString = new StandardCoder().encode(yamlObject);

            ToscaServiceTemplate toscaServiceTemplatePolicyType =
                    standardCoder.decode(yamlAsJsonString, ToscaServiceTemplate.class);

            assertNotNull(toscaServiceTemplatePolicyType);
            databaseProvider.createPolicyTypes(toscaServiceTemplatePolicyType);
        }
    }
}

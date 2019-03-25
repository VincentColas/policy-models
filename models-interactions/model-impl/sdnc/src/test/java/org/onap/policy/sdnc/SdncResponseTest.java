/*-
 * ============LICENSE_START=======================================================
 * sdnc
 * ================================================================================
 * Copyright (C) 2018 Huawei. All rights reserved.
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
 * ============LICENSE_END=========================================================
 */

package org.onap.policy.sdnc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;


import org.junit.Test;

public class SdncResponseTest {

    @Test
    public void testSdncResponse() {
        SdncResponse response = new SdncResponse();
        assertNotNull(response);
        assertNotEquals(0, response.hashCode());
        
        String requestId = "Get Home";
        response.setRequestId(requestId);
        assertEquals(requestId, response.getRequestId());
        
        SdncResponseOutput responseDescriptor = new SdncResponseOutput();
        response.setResponseOutput(responseDescriptor);
        assertEquals(responseDescriptor, response.getResponseOutput());
        
        assertNotEquals(0, response.hashCode());
    }
}

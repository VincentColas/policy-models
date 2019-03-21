/*
 * ============LICENSE_START=======================================================
 *  Copyright (C) 2019 Nordix Foundation.
 *  Modifications Copyright (C) 2019 AT&T Intellectual Property. All rights reserved.
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

package org.onap.policy.models.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;

/**
 * Test the PfUtils class.
 *
 * @author Liam Fallon (liam.fallon@est.tech)
 */
public class PfUtilsTest {

    @Test
    public void testCompareObjects() {
        assertEquals(0, PfUtils.compareObjects(null, null));
        assertEquals(-1, PfUtils.compareObjects("hello", null));
        assertEquals(1, PfUtils.compareObjects(null, "hello"));
        assertFalse(PfUtils.compareObjects("hello", "goodbye") == 0);
        assertEquals(0, PfUtils.compareObjects("hello", "hello"));
    }

    @Test
    public void testMapList() {
        assertNull(PfUtils.mapList(null, item -> {
            throw new RuntimeException("should not be invoked");
        }));

        List<String> origList = Arrays.asList("abc", "def");
        List<String> newList = PfUtils.mapList(origList, text -> text + "X");

        assertEquals(Arrays.asList("abcX", "defX"), newList);
    }
}

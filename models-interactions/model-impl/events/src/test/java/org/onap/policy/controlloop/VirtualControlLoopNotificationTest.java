/*-
 * ============LICENSE_START=======================================================
 * controlloop
 * ================================================================================
 * Copyright (C) 2018-2019 AT&T Intellectual Property. All rights reserved.
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

package org.onap.policy.controlloop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.Instant;
import java.util.Collections;
import org.junit.Test;

public class VirtualControlLoopNotificationTest {

    @Test
    public void test() {
        VirtualControlLoopNotification notification = new VirtualControlLoopNotification();
        assertNotNull(notification);

        notification.setAai(Collections.emptyMap());
        assertTrue(notification.getAai().isEmpty());

        Instant now = Instant.now();
        notification.setClosedLoopAlarmStart(now);

        notification.setClosedLoopAlarmEnd(now);

        VirtualControlLoopEvent event = new VirtualControlLoopEvent();

        Instant later = Instant.now();
        event.setAai(Collections.emptyMap());
        event.setClosedLoopAlarmStart(later);
        event.setClosedLoopAlarmEnd(later);

        notification = new VirtualControlLoopNotification(event);
        assertEquals(later, notification.getClosedLoopAlarmStart());
        assertEquals(later, notification.getClosedLoopAlarmEnd());

    }
}

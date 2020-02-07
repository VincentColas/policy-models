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

package org.onap.policy.controlloop.actorserviceprovider.parameters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.onap.policy.common.parameters.BeanValidationResult;
import org.onap.policy.controlloop.VirtualControlLoopEvent;
import org.onap.policy.controlloop.actorserviceprovider.ActorService;
import org.onap.policy.controlloop.actorserviceprovider.OperationOutcome;
import org.onap.policy.controlloop.actorserviceprovider.Operator;
import org.onap.policy.controlloop.actorserviceprovider.controlloop.ControlLoopEventContext;
import org.onap.policy.controlloop.actorserviceprovider.parameters.ControlLoopOperationParams.ControlLoopOperationParamsBuilder;
import org.onap.policy.controlloop.actorserviceprovider.spi.Actor;
import org.onap.policy.controlloop.policy.Target;

public class ControlLoopOperationParamsTest {
    private static final String EXPECTED_EXCEPTION = "expected exception";
    private static final String ACTOR = "my-actor";
    private static final String OPERATION = "my-operation";
    private static final Target TARGET = new Target();
    private static final String TARGET_ENTITY = "my-target";
    private static final Integer RETRY = 3;
    private static final Integer TIMEOUT = 100;
    private static final UUID REQ_ID = UUID.randomUUID();

    @Mock
    private Actor actor;

    @Mock
    private ActorService actorService;

    @Mock
    private Consumer<OperationOutcome> completer;

    @Mock
    private ControlLoopEventContext context;

    @Mock
    private VirtualControlLoopEvent event;

    @Mock
    private Executor executor;

    @Mock
    private CompletableFuture<OperationOutcome> operation;

    @Mock
    private Operator operator;

    @Mock
    private Consumer<OperationOutcome> starter;

    private Map<String, String> payload;

    private ControlLoopOperationParams params;
    private OperationOutcome outcome;


    /**
     * Initializes mocks and sets {@link #params} to a fully-loaded set of parameters.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(actorService.getActor(ACTOR)).thenReturn(actor);
        when(actor.getOperator(OPERATION)).thenReturn(operator);
        when(operator.startOperation(any())).thenReturn(operation);

        when(event.getRequestId()).thenReturn(REQ_ID);

        when(context.getEvent()).thenReturn(event);

        payload = new TreeMap<>();

        params = ControlLoopOperationParams.builder().actorService(actorService).completeCallback(completer)
                        .context(context).executor(executor).actor(ACTOR).operation(OPERATION).payload(payload)
                        .retry(RETRY).target(TARGET).targetEntity(TARGET_ENTITY).timeoutSec(TIMEOUT)
                        .startCallback(starter).build();

        outcome = params.makeOutcome();
    }

    @Test
    public void testStart() {
        assertSame(operation, params.start());

        assertThatIllegalArgumentException().isThrownBy(() -> params.toBuilder().context(null).build().start());
    }

    @Test
    public void testGetRequestId() {
        assertSame(REQ_ID, params.getRequestId());

        // try with null context
        assertNull(params.toBuilder().context(null).build().getRequestId());

        // try with null event
        when(context.getEvent()).thenReturn(null);
        assertNull(params.getRequestId());
    }

    @Test
    public void testMakeOutcome() {
        assertEquals(ACTOR, outcome.getActor());
        assertEquals(OPERATION, outcome.getOperation());
        checkRemainingFields("with actor");
    }

    protected void checkRemainingFields(String testName) {
        assertEquals(testName, TARGET_ENTITY, outcome.getTarget());
        assertNull(testName, outcome.getStart());
        assertNull(testName, outcome.getEnd());
        assertNull(testName, outcome.getSubRequestId());
        assertNotNull(testName, outcome.getResult());
        assertNull(testName, outcome.getMessage());
    }

    @Test
    public void testCallbackStarted() {
        params.callbackStarted(outcome);
        verify(starter).accept(outcome);

        // modify starter to throw an exception
        AtomicInteger count = new AtomicInteger();
        doAnswer(args -> {
            count.incrementAndGet();
            throw new IllegalStateException(EXPECTED_EXCEPTION);
        }).when(starter).accept(outcome);

        params.callbackStarted(outcome);
        verify(starter, times(2)).accept(outcome);
        assertEquals(1, count.get());

        // repeat with no start-callback - no additional calls expected
        params.toBuilder().startCallback(null).build().callbackStarted(outcome);
        verify(starter, times(2)).accept(outcome);
        assertEquals(1, count.get());

        // should not call complete-callback
        verify(completer, never()).accept(any());
    }

    @Test
    public void testCallbackCompleted() {
        params.callbackCompleted(outcome);
        verify(completer).accept(outcome);

        // modify completer to throw an exception
        AtomicInteger count = new AtomicInteger();
        doAnswer(args -> {
            count.incrementAndGet();
            throw new IllegalStateException(EXPECTED_EXCEPTION);
        }).when(completer).accept(outcome);

        params.callbackCompleted(outcome);
        verify(completer, times(2)).accept(outcome);
        assertEquals(1, count.get());

        // repeat with no complete-callback - no additional calls expected
        params.toBuilder().completeCallback(null).build().callbackCompleted(outcome);
        verify(completer, times(2)).accept(outcome);
        assertEquals(1, count.get());

        // should not call start-callback
        verify(starter, never()).accept(any());
    }

    @Test
    public void testValidateFields() {
        testValidate("actor", "null", bldr -> bldr.actor(null));
        testValidate("actorService", "null", bldr -> bldr.actorService(null));
        testValidate("context", "null", bldr -> bldr.context(null));
        testValidate("executor", "null", bldr -> bldr.executor(null));
        testValidate("operation", "null", bldr -> bldr.operation(null));
        testValidate("target", "null", bldr -> bldr.targetEntity(null));

        // check edge cases
        assertTrue(params.toBuilder().build().validate().isValid());

        // these can be null
        assertTrue(params.toBuilder().payload(null).retry(null).target(null).timeoutSec(null).startCallback(null)
                        .completeCallback(null).build().validate().isValid());

        // test with minimal fields
        assertTrue(ControlLoopOperationParams.builder().actorService(actorService).context(context).actor(ACTOR)
                        .operation(OPERATION).targetEntity(TARGET_ENTITY).build().validate().isValid());
    }

    private void testValidate(String fieldName, String expected,
                    Function<ControlLoopOperationParamsBuilder, ControlLoopOperationParamsBuilder> makeInvalid) {

        // original params should be valid
        BeanValidationResult result = params.validate();
        assertTrue(fieldName, result.isValid());

        // make invalid params
        result = makeInvalid.apply(params.toBuilder()).build().validate();
        assertFalse(fieldName, result.isValid());
        assertThat(result.getResult()).contains(fieldName).contains(expected);
    }

    @Test
    public void testBuilder_testToBuilder() {
        assertEquals(params, params.toBuilder().build());
    }

    @Test
    public void testGetActor() {
        assertSame(ACTOR, params.getActor());
    }

    @Test
    public void testGetActorService() {
        assertSame(actorService, params.getActorService());
    }

    @Test
    public void testGetContext() {
        assertSame(context, params.getContext());
    }

    @Test
    public void testGetExecutor() {
        assertSame(executor, params.getExecutor());

        // should use default when unspecified
        assertSame(ForkJoinPool.commonPool(), ControlLoopOperationParams.builder().build().getExecutor());
    }

    @Test
    public void testGetOperation() {
        assertSame(OPERATION, params.getOperation());
    }

    @Test
    public void testGetPayload() {
        assertSame(payload, params.getPayload());

        // should be null when unspecified
        assertNull(ControlLoopOperationParams.builder().build().getPayload());
    }

    @Test
    public void testGetRetry() {
        assertSame(RETRY, params.getRetry());

        // should be null when unspecified
        assertNull(ControlLoopOperationParams.builder().build().getRetry());
    }

    @Test
    public void testTarget() {
        assertSame(TARGET, params.getTarget());

        // should be null when unspecified
        assertNull(ControlLoopOperationParams.builder().build().getTarget());
    }

    @Test
    public void testGetTimeoutSec() {
        assertSame(TIMEOUT, params.getTimeoutSec());

        // should be 300 when unspecified
        assertEquals(Integer.valueOf(300), ControlLoopOperationParams.builder().build().getTimeoutSec());

        // null should be ok too
        assertNull(ControlLoopOperationParams.builder().timeoutSec(null).build().getTimeoutSec());
    }

    @Test
    public void testGetStartCallback() {
        assertSame(starter, params.getStartCallback());
    }

    @Test
    public void testGetCompleteCallback() {
        assertSame(completer, params.getCompleteCallback());
    }

    @Test
    public void testGetTargetEntity() {
        assertEquals(TARGET_ENTITY, params.getTargetEntity());
    }
}

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.logging.log4j.async.logger;

import org.apache.logging.log4j.core.util.Integers;
import org.apache.logging.log4j.kit.env.Log4jProperty;
import org.apache.logging.log4j.status.StatusLogger;
import org.jspecify.annotations.Nullable;

/**
 * Properties related to async loggers.
 */
@Log4jProperty(name = "async.logger")
public record AsyncLoggerProperties(
        @Nullable Class<? extends AsyncLoggerExceptionHandler> exceptionHandler,
        @Nullable Class<? extends AsyncLoggerConfigExceptionHandler> configExceptionHandler,
        @Log4jProperty(defaultValue = "4096") int ringBufferSize,
        @Log4jProperty(defaultValue = "true") boolean synchronizeEnqueueWhenQueueFull,
        WaitStrategyProperties waitStrategy) {

    private static final int RING_BUFFER_MIN_SIZE = 128;

    public int roundedRingBufferSize() {
        if (ringBufferSize < RING_BUFFER_MIN_SIZE) {
            StatusLogger.getLogger()
                    .warn("Invalid RingBuffer size {}, using minimum size {}.", ringBufferSize, RING_BUFFER_MIN_SIZE);
            return RING_BUFFER_MIN_SIZE;
        }
        return Integers.ceilingNextPowerOfTwo(ringBufferSize);
    }

    public record WaitStrategyProperties(
            @Log4jProperty(defaultValue = "TIMEOUT") String type,
            @Log4jProperty(defaultValue = "100") long sleepTimeNs,
            @Log4jProperty(defaultValue = "200") int retries,
            @Log4jProperty(defaultValue = "10") int timeout) {}
}

/**
 * Copyright 2015 Stuart Kent
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.github.stkent.amplify.tracking.rules;

import android.support.annotation.NonNull;

import com.github.stkent.amplify.tracking.interfaces.IEventBasedRule;

public final class IntervalCountRule implements IEventBasedRule<Integer> {

    private int minimumCount;
    private int maximumCount;
    private int interval;

    private IntervalCountRule(Builder builder) {
        minimumCount = builder.minimumCount;
        maximumCount = builder.maximumCount;
        interval = builder.interval;
    }

    public static Builder newBuilder() {
        return new Builder();
    }


    @Override
    public boolean shouldAllowFeedbackPromptByDefault() {
        return false;
    }

    @Override
    public boolean shouldAllowFeedbackPrompt(@NonNull final Integer cachedEventValue) {
        return (cachedEventValue == minimumCount && cachedEventValue < maximumCount) || (cachedEventValue > minimumCount && (cachedEventValue % interval == 0));
    }

    @NonNull
    @Override
    public String getDescription() {
        return "MinimumCountRule with minimum required count of " + minimumCount;
    }

    public static final class Builder {
        private int minimumCount;
        private int maximumCount;
        private int interval;

        private Builder() {
        }

        public Builder minimumCount(int val) {
            minimumCount = val;
            return this;
        }

        public Builder maximumCount(int val) {
            maximumCount = val;
            return this;
        }

        public Builder interval(int val) {
            interval = val;
            return this;
        }

        public IntervalCountRule build() {
            return new IntervalCountRule(this);
        }
    }
}

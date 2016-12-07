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
package com.github.stkent.amplify.tracking;

import android.support.annotation.NonNull;

import com.github.stkent.amplify.tracking.interfaces.IEvent;
import com.github.stkent.amplify.tracking.interfaces.IGlobalEvent;

import static com.github.stkent.amplify.utils.Constants.EXHAUSTIVE_SWITCH_EXCEPTION_MESSAGE;

public enum PromptViewEvent implements IGlobalEvent {

    PROMPT_SHOWN,
    THANKS_SHOWN,
    PROMPT_DISMISSED;

    private IEvent associatedEvent;

    @NonNull
    @Override
    public String getTrackingKey() {
        switch (this) {
            case PROMPT_SHOWN:
                if (getAssociatedEvent() != null) {
                    return "PROMPT_SHOWN_" + getAssociatedEvent().getTrackingKey();
                }

                return "PROMPT_SHOWN";
            case THANKS_SHOWN:
                if (getAssociatedEvent() != null) {
                    return "THANKS_SHOWN_" + getAssociatedEvent().getTrackingKey();
                }

                return "THANKS_SHOWN";
            case PROMPT_DISMISSED:
                if (getAssociatedEvent() != null) {
                    return "PROMPT_DISMISSED_" + getAssociatedEvent().getTrackingKey();
                }

                return "PROMPT_DISMISSED";
        }

        throw new IllegalStateException(EXHAUSTIVE_SWITCH_EXCEPTION_MESSAGE);
    }

    public IEvent getAssociatedEvent() {
        return associatedEvent;
    }

    public void setAssociatedEvent(IEvent associatedEvent) {
        this.associatedEvent = associatedEvent;
    }

    public static PromptViewEvent getPromptShownEvent(IEvent associatedEvent) {
        PromptViewEvent promptViewEvent = PROMPT_SHOWN;

        promptViewEvent.setAssociatedEvent(associatedEvent);

        return promptViewEvent;
    }

    @Override
    public String toString() {
        return getTrackingKey();
    }
}

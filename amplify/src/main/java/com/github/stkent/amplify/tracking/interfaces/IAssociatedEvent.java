package com.github.stkent.amplify.tracking.interfaces;

/**
 * Created by Cody on 11/14/16.
 */
public abstract class IAssociatedEvent implements IEvent {

    private IEvent associatedEvent;

    public IEvent getAssociatedEvent() {
        return associatedEvent;
    }

    public void setAssociatedEvent(IEvent associatedEvent) {
        this.associatedEvent = associatedEvent;
    }
}

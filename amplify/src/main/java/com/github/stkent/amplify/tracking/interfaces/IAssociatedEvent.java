package com.github.stkent.amplify.tracking.interfaces;

/**
 * Created by Cody on 11/14/16.
 */
public interface IAssociatedEvent extends IEvent {

    IEvent getAssociatedEvent();
    void setAssociatedEvent(IEvent event);
}

package com.w00tmast3r.skquery.elements.events.lang;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ReturnEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Event invoker;

    public ReturnEvent(Event invoker) {
        this.invoker = invoker;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Event getInvoker() {
        return invoker;
    }
}

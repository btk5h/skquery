package com.w00tmast3r.skquery.elements.events.lang;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ContinueEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

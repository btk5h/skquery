package com.w00tmast3r.skquery.elements.events.lang;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ScriptOptionsEvent extends Event {

    private static HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
